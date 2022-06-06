package io.github.fdelbrayelle.gdpr.security.oauth2.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN;

import io.github.fdelbrayelle.gdpr.IntegrationTest;
import io.github.fdelbrayelle.gdpr.security.oauth2.domain.AuthoritiesConstants;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

@IntegrationTest
class SecurityConfigurationIT {

  @Autowired
  GrantedAuthoritiesMapper grantedAuthoritiesMapper;

  @Test
  void shouldUserAuthoritiesMapperWithOidcUserAuthority() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("groups", List.of(AuthoritiesConstants.USER));
    claims.put("sub", 123);
    claims.put("preferred_username", "admin");
    OidcIdToken idToken = new OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims);

    OidcUserInfo userInfo = new OidcUserInfo(claims);

    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new OidcUserAuthority(AuthoritiesConstants.USER, idToken, userInfo));

    assertThatCode(() -> grantedAuthoritiesMapper.mapAuthorities(authorities)).doesNotThrowAnyException();
  }

  @Test
  void shouldUserAuthoritiesMapperWithSimpleGrantedAuthority() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

    assertThatCode(() -> grantedAuthoritiesMapper.mapAuthorities(authorities)).doesNotThrowAnyException();
  }
}
