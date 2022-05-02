package io.github.fdelbrayelle.gdpr.technical.infrastructure.primary.exception;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.fdelbrayelle.gdpr.UnitTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

@UnitTest
class BadRequestAlertExceptionTest {

  @Test
  void shouldBuild() {
    BadRequestAlertException exception = new BadRequestAlertException("chips", "beer", "validation");

    assertThat(exception.getTitle()).isEqualTo("chips");
    assertThat(exception.getEntityName()).isEqualTo("beer");
    assertThat(exception.getErrorKey()).isEqualTo("validation");
    assertThat(exception.getParameters()).containsExactlyInAnyOrderEntriesOf(Map.of("message", "error.validation", "params", "beer"));
  }
}
