package io.github.fdelbrayelle.gdpr.user.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  public void shouldPseudonymizeLastname() {
    // Given
    var user = User.builder().lastname("Wayne").firstname("Bruce").build();

    // When
    String pseudonymizedLastname = user.getLastname();

    // Then
    assertThat(pseudonymizedLastname).isEqualTo("ccuT1cMTDjiNHJfzCSco2g==");
  }
}
