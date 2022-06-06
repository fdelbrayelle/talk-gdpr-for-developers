package io.github.fdelbrayelle.gdpr.security.oauth2.domain;

public class ApplicationSecurityDefaults {

  private ApplicationSecurityDefaults() {}

  public static final String CONTENT_SECURITY_POLICY =
    "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:";
}
