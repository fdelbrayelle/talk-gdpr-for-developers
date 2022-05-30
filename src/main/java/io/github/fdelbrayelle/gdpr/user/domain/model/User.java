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
public class User {

  @JsonIgnore
  UUID id;

  String lastname;
  String firstname;
  String phoneNumber;
  Address address;
  Sex sex;
}
