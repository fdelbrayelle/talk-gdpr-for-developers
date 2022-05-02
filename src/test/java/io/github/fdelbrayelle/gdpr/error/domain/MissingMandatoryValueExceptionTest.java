package io.github.fdelbrayelle.gdpr.error.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.fdelbrayelle.gdpr.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class MissingMandatoryValueExceptionTest {

  public static final String FIELD = "field";

  @Test
  void shouldGetExceptionInformation() {
    MissingMandatoryValueException exception = new MissingMandatoryValueException(FIELD);
    assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set");
  }

  @Test
  void shouldGetExceptionForBlankValue() {
    MissingMandatoryValueException exception = MissingMandatoryValueException.forBlankValue(FIELD);

    assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set (blank)");
  }

  @Test
  void shouldGetExceptionForNullValue() {
    MissingMandatoryValueException exception = MissingMandatoryValueException.forNullValue(FIELD);

    assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set (null)");
  }

  @Test
  void shouldGetExceptionForEmptyCollection() {
    MissingMandatoryValueException exception = MissingMandatoryValueException.forEmptyValue(FIELD);

    assertThat(exception.getMessage()).isEqualTo("The field \"field\" is mandatory and wasn't set (empty)");
  }
}
