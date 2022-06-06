package io.github.fdelbrayelle.gdpr.security.oauth2.infrastructure.config;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.fdelbrayelle.gdpr.UnitTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Test class for the {@link AudienceValidator} utility class.
 */
@UnitTest
class AudienceValidatorTest {

  private final AudienceValidator validator = new AudienceValidator(List.of("api://default"));

  @Test
  void shouldInvalidAudience() {
    Jwt badJwt = mock(Jwt.class);
    when(badJwt.getAudience()).thenReturn(List.of("bar"));

    assertThat(validator.validate(badJwt).hasErrors()).isTrue();
  }

  @Test
  void shouldValidAudience() {
    Jwt jwt = mock(Jwt.class);
    when(jwt.getAudience()).thenReturn(List.of("api://default"));

    assertThat(validator.validate(jwt).hasErrors()).isFalse();
  }
}
