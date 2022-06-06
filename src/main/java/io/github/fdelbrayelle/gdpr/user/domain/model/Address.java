package io.github.fdelbrayelle.gdpr.user.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

  @JsonIgnore
  UUID id;

  Integer number;
  String street;
  String city;
  String zipCode;
  String country;
}
