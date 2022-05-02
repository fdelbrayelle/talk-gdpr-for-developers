package io.github.fdelbrayelle.gdpr.technical.infrastructure.primary.exception;

import static org.assertj.core.api.Assertions.*;

import io.github.fdelbrayelle.gdpr.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class FieldErrorDTOTest {

  @Test
  void shouldBuild() {
    FieldErrorDTO fieldErrorDTO = new FieldErrorDTO("dto", "field", "message");

    assertThat(fieldErrorDTO.getObjectName()).isEqualTo("dto");
    assertThat(fieldErrorDTO.getField()).isEqualTo("field");
    assertThat(fieldErrorDTO.getMessage()).isEqualTo("message");
  }
}
