package io.github.fdelbrayelle.gdpr.user.domain.port;

import io.github.fdelbrayelle.gdpr.user.domain.model.User;

public interface UserService {
  User addUser(User user);
}
