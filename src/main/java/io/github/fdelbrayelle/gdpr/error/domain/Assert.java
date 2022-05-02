package io.github.fdelbrayelle.gdpr.error.domain;

import java.util.Collection;

public class Assert {

  private Assert() {}

  public static void notNull(String field, Object input) {
    if (input == null) {
      throw MissingMandatoryValueException.forNullValue(field);
    }
  }

  public static void notBlank(String field, String input) {
    notNull(field, input);

    if (input.isBlank()) {
      throw MissingMandatoryValueException.forBlankValue(field);
    }
  }

  public static void notEmpty(String field, Collection<?> collection) {
    notNull(field, collection);

    if (collection.isEmpty()) {
      throw MissingMandatoryValueException.forEmptyValue(field);
    }
  }

  public static void notNegative(String field, Integer input) {
    notNull(field, input);

    if (input < 0) {
      throw UnauthorizedValueException.forNegativeValue(field);
    }
  }

  public static void notGreaterThan(String field, Integer input, int ceil) {
    notNull(field, input);

    if (input > ceil) {
      throw UnauthorizedValueException.forInconsistentValue(field, input);
    }
  }

  public static void notLowerThan(String field, Integer input, int floor) {
    notNull(field, input);

    if (input < floor) {
      throw UnauthorizedValueException.forInconsistentValue(field, input);
    }
  }

  public static void areEqual(String field1, String field2, Object input1, Object input2) {
    notNull(field1, input1);
    notNull(field2, input2);

    if (!input1.equals(input2)) {
      throw UnauthorizedValueException.forUnequalValues(field1, field2, input1, input2);
    }
  }

  public static void hasMinimumLength(String field, int min, String input) {
    notNull(field, input);

    if (input.length() < min) {
      throw UnauthorizedValueException.forTooShortValue(field);
    }
  }

  public static void isEqualTo(String field, Object input, Object expectedValue) {
    notNull(field, input);
    if (!input.equals(expectedValue)) {
      throw UnauthorizedValueException.forUnexpectedValue(field, input, expectedValue);
    }
  }
}
