package io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.entity;

import io.github.fdelbrayelle.gdpr.user.domain.model.Sex;
import io.github.fdelbrayelle.gdpr.user.domain.model.User;
import java.util.UUID;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  private String lastname;

  private String firstname;

  @Column(name = "phone_number")
  private String phoneNumber;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  @ToString.Exclude
  private AddressEntity address;

  @Enumerated(EnumType.STRING)
  private Sex sex;

  public static UserEntity fromDomain(final User user) {
    return UserEntity
      .builder()
      .lastname(user.getLastname())
      .firstname(user.getFirstname())
      .phoneNumber(user.getPhoneNumber())
      .address(AddressEntity.fromDomain(user.getAddress()))
      .sex(user.getSex())
      .build();
  }

  public static User toDomain(final UserEntity userEntity) {
    return User
      .builder()
      .id(userEntity.getId())
      .lastname(userEntity.getLastname())
      .firstname(userEntity.getFirstname())
      .phoneNumber(userEntity.getPhoneNumber())
      .address(AddressEntity.toDomain(userEntity.getAddress()))
      .sex(userEntity.getSex())
      .build();
  }
}
