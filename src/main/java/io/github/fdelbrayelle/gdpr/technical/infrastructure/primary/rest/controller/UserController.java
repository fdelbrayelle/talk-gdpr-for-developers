package io.github.fdelbrayelle.gdpr.technical.infrastructure.primary.rest.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.github.fdelbrayelle.gdpr.user.domain.model.User;
import io.github.fdelbrayelle.gdpr.user.domain.port.UserService;
import java.net.URI;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(final UserService userService) {
    this.userService = userService;
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<User> addUser(@Valid @RequestBody final User user) {
    var createdUser = userService.addUser(user);
    log.info("The following user has been created: {}", user);
    final URI userLocation = URI.create(String.format("/api/users/%s", createdUser.getId()));
    return ResponseEntity.created(userLocation).build();
  }
}
