package io.github.fdelbrayelle.gdpr.error.domain;

import static org.assertj.core.api.Assertions.*;

import io.github.fdelbrayelle.gdpr.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class AccountExceptionTest {

  @Test
  void shouldGetAccountException() {
    AccountException exception = new AccountException();
    assertThat(exception.getMessage()).isNull();
  }

  @Test
  void shouldAccountExceptionWithMessage() {
    AccountException exception = new AccountException("Hello JHipster");
    assertThat(exception.getMessage()).isEqualTo("Hello JHipster");
  }

  @Test
  void shouldAccountExceptionWithCause() {
    NullPointerException nullPointerException = new NullPointerException();
    AccountException exception = new AccountException("Hello JHipster", nullPointerException);

    assertThat(exception.getMessage()).isEqualTo("Hello JHipster");
    assertThat(exception.getCause()).isInstanceOf(NullPointerException.class);
  }
}
