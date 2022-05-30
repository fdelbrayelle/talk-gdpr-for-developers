package io.github.fdelbrayelle.gdpr.user.domain.config;

import io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.adapter.UserServiceImpl;
import io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.repository.UserRepository;
import io.github.fdelbrayelle.gdpr.user.domain.port.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserBeanConfiguration {

  @Bean
  public UserService userService(final UserRepository userRepository) {
    return new UserServiceImpl(userRepository);
  }
}
