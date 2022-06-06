package io.github.fdelbrayelle.gdpr.security.oauth2.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames.ID_TOKEN;

import io.github.fdelbrayelle.gdpr.UnitTest;
import io.github.fdelbrayelle.gdpr.error.domain.AccountException;
import io.github.fdelbrayelle.gdpr.error.domain.MissingMandatoryValueException;
import io.github.fdelbrayelle.gdpr.security.oauth2.domain.AuthoritiesConstants;
import java.time.Instant;
import java.util.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@UnitTest
class SecurityUtilsTest {

  @BeforeEach
  @AfterEach
  void cleanup() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldNotGetAuthorities() {
    assertThat(SecurityUtils.getAuthorities()).isEmpty();
  }

  @Test
  void shouldNotGetCurrentUserLoginForNull() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(null);
    SecurityContextHolder.setContext(securityContext);
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    assertThat(login).isEmpty();

    assertThat(SecurityUtils.getAuthorities()).isEmpty();
  }

  @Test
  void shouldGetCurrentUserLoginWithUserDetails() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    UsernamePasswordAuthenticationToken token = buildUsernamePasswordAuthenticationToken();
    securityContext.setAuthentication(token);
    SecurityContextHolder.setContext(securityContext);
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    assertThat(login).contains("admin");

    assertThat(SecurityUtils.getAuthorities()).contains(AuthoritiesConstants.ADMIN);
  }

  @Test
  void shouldGetCurrentUserLoginWithJwtAuthenticationToken() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    JwtAuthenticationToken token = buildJwtAuthenticationToken();
    securityContext.setAuthentication(token);
    SecurityContextHolder.setContext(securityContext);

    assertThat(SecurityUtils.getCurrentUserLogin()).contains("admin");
    assertThat(SecurityUtils.getAuthorities()).contains(AuthoritiesConstants.ADMIN);
    assertThat(SecurityUtils.isAuthenticated()).isTrue();
  }

  @Test
  void shouldGetCurrentUserLoginForOAuth2() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    OAuth2AuthenticationToken auth2AuthenticationToken = buildOAuth2AuthenticationToken();
    securityContext.setAuthentication(auth2AuthenticationToken);
    SecurityContextHolder.setContext(securityContext);

    assertThat(SecurityUtils.getCurrentUserLogin()).contains("admin");
    assertThat(SecurityUtils.getAuthorities()).contains(AuthoritiesConstants.USER);
    assertThat(SecurityUtils.isAuthenticated()).isTrue();
  }

  @Test
  void shouldNotGetCurrentUserLoginForOAuth2() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Map<String, Object> claims = new HashMap<>();
    claims.put("groups", AuthoritiesConstants.USER);
    claims.put("sub", 123);
    OidcIdToken idToken = new OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
    OidcUser user = new DefaultOidcUser(authorities, idToken);
    OAuth2AuthenticationToken auth2AuthenticationToken = new OAuth2AuthenticationToken(user, authorities, "oidc");
    securityContext.setAuthentication(auth2AuthenticationToken);
    SecurityContextHolder.setContext(securityContext);

    Optional<String> login = SecurityUtils.getCurrentUserLogin();

    assertThat(login).isEmpty();
  }

  @Test
  void shouldGetCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    assertThat(login).contains("admin");

    assertThat(SecurityUtils.getAuthorities()).isEmpty();
  }

  @Test
  void shouldNotGetCurrentUserLoginForAnotherInstance() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new TestingAuthenticationToken(null, null));
    SecurityContextHolder.setContext(securityContext);

    Optional<String> login = SecurityUtils.getCurrentUserLogin();
    assertThat(login).isEmpty();

    assertThat(SecurityUtils.getAuthorities()).isEmpty();
  }

  @Test
  void shouldExtractAuthorityFromClaims() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("groups", Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER));

    List<GrantedAuthority> expectedAuthorities = Arrays.asList(
      new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN),
      new SimpleGrantedAuthority(AuthoritiesConstants.USER)
    );

    List<GrantedAuthority> authorities = SecurityUtils.extractAuthorityFromClaims(claims);

    assertThat(authorities).isNotNull().isNotEmpty().hasSize(2).containsAll(expectedAuthorities);
  }

  @Test
  void shouldExtractAuthorityFromClaimsNamespacedRoles() {
    Map<String, Object> claims = new HashMap<>();
    claims.put(SecurityUtils.CLAIMS_NAMESPACE + "roles", Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER));

    List<GrantedAuthority> expectedAuthorities = Arrays.asList(
      new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN),
      new SimpleGrantedAuthority(AuthoritiesConstants.USER)
    );

    List<GrantedAuthority> authorities = SecurityUtils.extractAuthorityFromClaims(claims);

    assertThat(authorities).isNotNull().isNotEmpty().hasSize(2).containsAll(expectedAuthorities);
  }

  @Test
  void shouldBeAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtils.isAuthenticated();
    assertThat(isAuthenticated).isTrue();
  }

  @Test
  void shouldNotBeAuthenticatedWithNull() {
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    boolean isAuthenticated = SecurityUtils.isAuthenticated();
    assertThat(isAuthenticated).isFalse();
  }

  @Test
  void shouldAnonymousIsNotAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtils.isAuthenticated();
    assertThat(isAuthenticated).isFalse();
  }

  @Test
  void shouldHasCurrentUserThisAuthority() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
    SecurityContextHolder.setContext(securityContext);

    assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.USER)).isTrue();
    assertThat(SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)).isFalse();
  }

  @Test
  void shouldHasCurrentUserAnyOfAuthorities() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
    SecurityContextHolder.setContext(securityContext);

    assertThat(SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)).isTrue();
    assertThat(SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.ANONYMOUS, AuthoritiesConstants.ADMIN)).isFalse();
  }

  @Test
  void shouldNotHaveCurrentUserAnyOfAuthoritiesForNull() {
    SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());

    assertThat(SecurityUtils.hasCurrentUserAnyOfAuthorities(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)).isFalse();
  }

  @Test
  void shouldHasCurrentUserNoneOfAuthorities() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user", "user", authorities));
    SecurityContextHolder.setContext(securityContext);

    assertThat(SecurityUtils.hasCurrentUserNoneOfAuthorities(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN)).isFalse();
    assertThat(SecurityUtils.hasCurrentUserNoneOfAuthorities(AuthoritiesConstants.ANONYMOUS, AuthoritiesConstants.ADMIN)).isTrue();
  }

  @Test
  @DisplayName("should get attributes for OAuth2")
  void shouldGetAttributesForOAuth2() {
    OAuth2AuthenticationToken token = buildOAuth2AuthenticationToken();

    Map<String, Object> attributes = SecurityUtils.getAttributes(token);

    assertThat(attributes).isNotEmpty().containsEntry("preferred_username", "admin");
  }

  @Test
  @DisplayName("should get attributes for JWT")
  void shouldGetAttributesForJWT() {
    JwtAuthenticationToken token = buildJwtAuthenticationToken();

    Map<String, Object> attributes = SecurityUtils.getAttributes(token);

    assertThat(attributes).isNotEmpty().containsEntry("preferred_username", "admin");
  }

  @Test
  void shouldNotGetAttributesForAnotherToken() {
    UsernamePasswordAuthenticationToken token = buildUsernamePasswordAuthenticationToken();

    Assertions.assertThatThrownBy(() -> SecurityUtils.getAttributes(token)).isExactlyInstanceOf(AccountException.class);
  }

  @Test
  void shouldNotGetAttributesForNull() {
    Assertions.assertThatThrownBy(() -> SecurityUtils.getAttributes(null)).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  private OAuth2AuthenticationToken buildOAuth2AuthenticationToken() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("groups", AuthoritiesConstants.USER);
    claims.put("sub", 123);
    claims.put("preferred_username", "admin");
    OidcIdToken idToken = new OidcIdToken(ID_TOKEN, Instant.now(), Instant.now().plusSeconds(60), claims);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
    OidcUser user = new DefaultOidcUser(authorities, idToken);
    return new OAuth2AuthenticationToken(user, authorities, "oidc");
  }

  private JwtAuthenticationToken buildJwtAuthenticationToken() {
    Jwt jwt = Jwt
      .withTokenValue("token")
      .header("alg", JwsAlgorithms.RS256)
      .subject("jhipster")
      .claim("preferred_username", "admin")
      .build();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
    return new JwtAuthenticationToken(jwt, authorities);
  }

  private UsernamePasswordAuthenticationToken buildUsernamePasswordAuthenticationToken() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
    User user = new User("admin", "admin", authorities);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "admin", authorities);
    return token;
  }
}
