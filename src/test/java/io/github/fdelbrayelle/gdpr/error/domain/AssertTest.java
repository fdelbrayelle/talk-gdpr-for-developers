package io.github.fdelbrayelle.gdpr.error.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.fdelbrayelle.gdpr.UnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class AssertTest {

  public static final String NOT_NULL_OR_EMPTY = "NotNullOrEmpty";

  @Test
  void shouldNotValidateNullInputs() {
    assertThatThrownBy(() -> Assert.notNull("field", null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("\"field\"");
  }

  @Test
  void shouldValidateNonNull() {
    assertThatCode(() -> Assert.notNull("field", NOT_NULL_OR_EMPTY)).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateNullString() {
    assertThatThrownBy(() -> Assert.notBlank("field", null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("\"field\"")
      .hasMessageContaining("(null)");
  }

  @Test
  void shouldNotValidateEmptyString() {
    assertNotBlankString("");
  }

  @Test
  void shouldNotValidateSpaceString() {
    assertNotBlankString(" ");
  }

  @Test
  void shouldNotValidateTabString() {
    assertNotBlankString("\t");
  }

  @Test
  void shouldValidateNonBlank() {
    assertThatCode(() -> Assert.notBlank("field", NOT_NULL_OR_EMPTY)).doesNotThrowAnyException();
  }

  private void assertNotBlankString(String input) {
    assertThatThrownBy(() -> Assert.notBlank("field", input))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("\"field\"")
      .hasMessageContaining("(blank)");
  }

  @Test
  void shouldNotValidateNegative() {
    assertThatThrownBy(() -> Assert.notNegative("field", -1))
      .isExactlyInstanceOf(UnauthorizedValueException.class)
      .hasMessageContaining("(negative)");
  }

  @Test
  void shouldValidatePositive() {
    assertThatCode(() -> Assert.notNegative("field", 11)).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateValueGreaterThanCeil() {
    assertThatThrownBy(() -> Assert.notGreaterThan("field", 2, 1))
      .isExactlyInstanceOf(UnauthorizedValueException.class)
      .hasMessageContaining("inconsistent");
  }

  @Test
  void shouldValidateValueGreaterThan() {
    assertThatCode(() -> Assert.notGreaterThan("field", 1, 2)).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateValueLowerThanFloor() {
    assertThatThrownBy(() -> Assert.notLowerThan("field", 0, 1))
      .isExactlyInstanceOf(UnauthorizedValueException.class)
      .hasMessageContaining("inconsistent");
  }

  @Test
  void shouldValidateValueLowerThanFloor() {
    assertThatCode(() -> Assert.notLowerThan("field", 0, 0)).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateValueNotEquals() {
    assertThatThrownBy(() -> Assert.areEqual("field1", "field2", 1, 2))
      .isExactlyInstanceOf(UnauthorizedValueException.class)
      .hasMessageContaining("not equal");
  }

  @Test
  void shouldValidateValueEquals() {
    assertThatCode(() -> Assert.areEqual("field1", "field2", 1, 1)).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateEmptyCollection() {
    List<String> list = List.of();
    assertThatThrownBy(() -> Assert.notEmpty("field", list))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("empty");
  }

  @Test
  void shouldValidateNotEmptyCollection() {
    assertThatCode(() -> Assert.notEmpty("field", List.of("Hello"))).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateTooShortString() {
    assertThatThrownBy(() -> Assert.hasMinimumLength("field", 10, "dix"))
      .isExactlyInstanceOf(UnauthorizedValueException.class)
      .hasMessageContaining("too short");
  }

  @Test
  void shouldValidateHasMinimumLength() {
    assertThatCode(() -> Assert.hasMinimumLength("field", 4, "four")).doesNotThrowAnyException();
    assertThatCode(() -> Assert.hasMinimumLength("field", 3, "three")).doesNotThrowAnyException();
  }

  @Test
  void shouldNotValidateIsEqualTo() {
    assertThatThrownBy(() -> Assert.isEqualTo("field", 3, 4))
      .isExactlyInstanceOf(UnauthorizedValueException.class)
      .hasMessageContaining("field")
      .hasMessageContaining("expected");
  }

  @Test
  void shouldValidateIsEqualTo() {
    assertThatCode(() -> Assert.isEqualTo("field", "radish", "radish")).doesNotThrowAnyException();
  }
}
