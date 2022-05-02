package io.github.fdelbrayelle.gdpr.error.domain;

public class MissingMandatoryValueException extends RuntimeException {

  public MissingMandatoryValueException(String field) {
    this(new RuntimeException(defaultMessage(field)));
  }

  protected MissingMandatoryValueException(RuntimeException runtimeException) {
    super(runtimeException.getMessage());
  }

  private static String defaultMessage(String field) {
    return "The field \"" + field + "\" is mandatory and wasn't set";
  }

  public static MissingMandatoryValueException forBlankValue(String field) {
    return new MissingMandatoryValueException(new RuntimeException(defaultMessage(field) + " (blank)"));
  }

  public static MissingMandatoryValueException forNullValue(String field) {
    return new MissingMandatoryValueException(new RuntimeException(defaultMessage(field) + " (null)"));
  }

  public static MissingMandatoryValueException forEmptyValue(String field) {
    return new MissingMandatoryValueException(new RuntimeException(defaultMessage(field) + " (empty)"));
  }
}
