package io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.liquibase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import io.github.fdelbrayelle.gdpr.LogbackRecorder;
import io.github.fdelbrayelle.gdpr.LogbackRecorder.Event;
import io.github.fdelbrayelle.gdpr.UnitTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mock.env.MockEnvironment;

@UnitTest
class AsyncSpringLiquibaseTest {

  private LiquibaseException exception = new LiquibaseException("Eek");

  private SimpleAsyncTaskExecutor executor;
  private ConfigurableEnvironment environment;
  private TestAsyncSpringLiquibase config;
  private TestBadConnectionSpringLiquibase configWithBadConnection;
  private LogbackRecorder recorder;
  private LiquibaseProperties liquibaseProperties;

  @BeforeEach
  void setup() {
    executor = new SimpleAsyncTaskExecutor();
    recorder = LogbackRecorder.forClass(MockEnvironment.class).reset().capture("ALL");
    environment = new MockEnvironment();
    recorder.release();
    liquibaseProperties = new LiquibaseProperties();
    config = spy(new TestAsyncSpringLiquibase(executor, environment, liquibaseProperties));
    configWithBadConnection = spy(new TestBadConnectionSpringLiquibase(executor, environment, liquibaseProperties));
    recorder = LogbackRecorder.forClass(AsyncSpringLiquibase.class).reset().capture("ALL");
  }

  @AfterEach
  void teardown() {
    recorder.release();
  }

  @Test
  void shouldInitDbWithDefault() {
    Throwable caught;
    synchronized (executor) {
      caught =
        catchThrowable(() -> {
          config.afterPropertiesSet();
          executor.wait(100);
        });
      assertThat(caught).isNull();
    }

    caught = catchThrowable(() -> verify(config).initDb());
    assertThat(caught).isNull();

    List<Event> events = recorder.play();
    assertThat(events).hasSize(2);
    Event event0 = events.get(0);
    assertThat(event0.getLevel()).isEqualTo("DEBUG");
    assertThat(event0.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTING_SYNC_MESSAGE);
    assertThat(event0.getThrown()).isNull();
    Event event1 = events.get(1);
    assertThat(event1.getLevel()).isEqualTo("DEBUG");
    assertThat(event1.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTED_MESSAGE);
    assertThat(event1.getThrown()).isNull();
  }

  @Test
  void shouldInitDbWithLocalProfile() {
    environment.setActiveProfiles("local");

    Throwable caught;
    synchronized (executor) {
      caught =
        catchThrowable(() -> {
          config.afterPropertiesSet();
          executor.wait(100);
        });
      assertThat(caught).isNull();
    }

    caught = catchThrowable(() -> verify(config).initDb());
    assertThat(caught).isNull();

    List<Event> events = recorder.play();
    assertThat(events).hasSize(2);
    Event event0 = events.get(0);
    assertThat(event0.getLevel()).isEqualTo("WARN");
    assertThat(event0.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTING_ASYNC_MESSAGE);
    assertThat(event0.getThrown()).isNull();
    Event event1 = events.get(1);
    assertThat(event1.getLevel()).isEqualTo("DEBUG");
    assertThat(event1.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTED_MESSAGE);
    assertThat(event1.getThrown()).isNull();
  }

  @Test
  void shouldInitDbWhenSlow() {
    environment.setActiveProfiles("local");
    doReturn(AsyncSpringLiquibase.SLOWNESS_THRESHOLD * 1000L + 100L).when(config).getSleep();
    Throwable caught;

    synchronized (executor) {
      caught =
        catchThrowable(() -> {
          config.afterPropertiesSet();
          executor.wait(config.getSleep() + 100L);
        });
      assertThat(caught).isNull();
    }

    caught = catchThrowable(() -> verify(config).initDb());
    assertThat(caught).isNull();

    List<Event> events = recorder.play();
    assertThat(events).hasSize(3);
    Event event0 = events.get(0);
    assertThat(event0.getLevel()).isEqualTo("WARN");
    assertThat(event0.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTING_ASYNC_MESSAGE);
    assertThat(event0.getThrown()).isNull();
    Event event1 = events.get(1);
    assertThat(event1.getLevel()).isEqualTo("DEBUG");
    assertThat(event1.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTED_MESSAGE);
    assertThat(event1.getThrown()).isNull();
    Event event2 = events.get(2);
    assertThat(event2.getLevel()).isEqualTo("WARN");
    assertThat(event2.getMessage()).isEqualTo(AsyncSpringLiquibase.SLOWNESS_MESSAGE);
    assertThat(event2.getThrown()).isNull();
  }

  @Test
  void shouldInitDbWhenDisable() {
    liquibaseProperties.setEnabled(false);

    Throwable caught;
    synchronized (executor) {
      caught =
        catchThrowable(() -> {
          config.afterPropertiesSet();
          executor.wait(100);
        });
      assertThat(caught).isNull();
    }

    caught = catchThrowable(() -> verify(config, times(0)).initDb());
    assertThat(caught).isNull();
  }

  @Test
  void testConnexionException() {
    environment.setActiveProfiles("local");

    Throwable caught;
    synchronized (executor) {
      caught =
        catchThrowable(() -> {
          configWithBadConnection.afterPropertiesSet();
          executor.wait(100);
        });
      assertThat(caught).isNull();
    }

    caught = catchThrowable(() -> verify(configWithBadConnection).initDb());
    assertThat(caught).isNotNull();
  }

  @Test
  void shouldInitDbWithLiquibaseException() {
    environment.setActiveProfiles("local");
    Throwable caught = catchThrowable(() -> doThrow(exception).when(config).initDb());
    assertThat(caught).isNull();

    synchronized (executor) {
      caught =
        catchThrowable(() -> {
          config.afterPropertiesSet();
          executor.wait(100);
        });
      assertThat(caught).isNull();
    }

    caught = catchThrowable(() -> verify(config).initDb());
    assertThat(caught).isNull();

    List<Event> events = recorder.play();
    assertThat(events).hasSize(2);
    Event event0 = events.get(0);
    assertThat(event0.getLevel()).isEqualTo("WARN");
    assertThat(event0.getMessage()).isEqualTo(AsyncSpringLiquibase.STARTING_ASYNC_MESSAGE);
    assertThat(event0.getThrown()).isNull();
    Event event1 = events.get(1);
    assertThat(event1.getLevel()).isEqualTo("ERROR");
    assertThat(event1.getMessage()).isEqualTo(AsyncSpringLiquibase.EXCEPTION_MESSAGE);
    assertThat(event1.getThrown()).isEqualTo(exception.toString());
  }

  private class TestAsyncSpringLiquibase extends AsyncSpringLiquibase {

    public TestAsyncSpringLiquibase(TaskExecutor executor, Environment environment, LiquibaseProperties liquibaseProperties) {
      super(executor, environment, liquibaseProperties);
    }

    @Override
    protected void initDb() throws LiquibaseException {
      synchronized (executor) {
        super.initDb();
        executor.notifyAll();
      }
    }

    @Override
    public DataSource getDataSource() {
      DataSource source = mock(DataSource.class);
      try {
        doReturn(mock(Connection.class)).when(source).getConnection();
      } catch (SQLException x) {
        // This should never happen
        throw new Error(x);
      }
      return source;
    }

    @Override
    protected Liquibase createLiquibase(Connection c) {
      return null;
    }

    @Override
    @SuppressWarnings("java:S2925")
    protected void performUpdate(Liquibase liquibase) {
      long sleep = getSleep();
      if (sleep > 0) {
        try {
          Thread.sleep(sleep);
        } catch (InterruptedException x) {
          // This should never happen
          throw new Error(x);
        }
      }
    }

    long getSleep() {
      return 0L;
    }
  }

  private class TestBadConnectionSpringLiquibase extends AsyncSpringLiquibase {

    public TestBadConnectionSpringLiquibase(TaskExecutor executor, Environment environment, LiquibaseProperties liquibaseProperties) {
      super(executor, environment, liquibaseProperties);
    }

    @Override
    protected void initDb() throws LiquibaseException {
      synchronized (executor) {
        super.initDb();
        executor.notifyAll();
      }
    }

    @Override
    public DataSource getDataSource() {
      DataSource source = mock(DataSource.class);
      try {
        doThrow(new SQLException("error")).when(source).getConnection();
      } catch (SQLException x) {
        // This should never happen
        throw new Error(x);
      }
      return source;
    }

    @Override
    protected Liquibase createLiquibase(Connection c) {
      return null;
    }

    @Override
    @SuppressWarnings("java:S2925")
    protected void performUpdate(Liquibase liquibase) {
      long sleep = getSleep();
      if (sleep > 0) {
        try {
          Thread.sleep(sleep);
        } catch (InterruptedException x) {
          // This should never happen
          throw new Error(x);
        }
      }
    }

    long getSleep() {
      return 0L;
    }
  }
}
