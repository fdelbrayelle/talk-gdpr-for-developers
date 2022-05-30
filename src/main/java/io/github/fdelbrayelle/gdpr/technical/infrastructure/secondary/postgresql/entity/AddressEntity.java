package io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.entity;

import io.github.fdelbrayelle.gdpr.user.domain.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "address")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "address_number")
  private Integer number;

  private String street;

  private String city;

  @Column(name = "zip_code")
  private String zipCode;

  private String country;

  public static AddressEntity fromDomain(final Address address) {
    return AddressEntity.builder()
      .number(address.getNumber())
      .street(address.getStreet())
      .city(address.getCity())
      .zipCode(address.getZipCode())
      .country(address.getCountry())
      .build();
  }

  public static Address toDomain(final AddressEntity addressEntity) {
    return Address.builder()
      .id(addressEntity.getId())
      .number(addressEntity.getNumber())
      .street(addressEntity.getStreet())
      .city(addressEntity.getCity())
      .zipCode(addressEntity.getZipCode())
      .country(addressEntity.getCountry())
      .build();
  }
}
