package io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.adapter;

import io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.entity.UserEntity;
import io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.repository.UserRepository;
import io.github.fdelbrayelle.gdpr.user.domain.model.User;
import io.github.fdelbrayelle.gdpr.user.domain.port.UserService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

  public UserServiceImpl(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User addUser(final User user) {
    var createdUserEntity = userRepository.save(UserEntity.fromDomain(user));
    return UserEntity.toDomain(createdUserEntity);
  }
}
