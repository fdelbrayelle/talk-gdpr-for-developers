package io.github.fdelbrayelle.gdpr.security.oauth2.infrastructure.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.fdelbrayelle.gdpr.security.oauth2.application.SecurityUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Claim converter to add custom claims by retrieving the user from the userinfo endpoint.
 */
public class CustomClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {

  public static final String GIVEN_NAME = "given_name";
  public static final String FAMILY_NAME = "family_name";
  public static final String EMAIL = "email";
  public static final String GROUPS = "groups";
  public static final String NAME = "name";
  public static final String PREFERRED_USERNAME = "preferred_username";
  public static final String ROLES = "roles";
  public static final String SUB = "sub";
  private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

  private final MappedJwtClaimSetConverter delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap());

  private final RestTemplate restTemplate;

  private final ClientRegistration registration;

  private final Map<String, ObjectNode> users = new ConcurrentHashMap<>();

  public CustomClaimConverter(ClientRegistration registration, RestTemplate restTemplate) {
    this.registration = registration;
    this.restTemplate = restTemplate;
  }

  public Map<String, Object> convert(Map<String, Object> claims) {
    Map<String, Object> convertedClaims = this.delegate.convert(claims);
    if (
      RequestContextHolder.getRequestAttributes() != null &&
      RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes
    ) {
      // Retrieve and set the token
      String token = bearerTokenResolver.resolve(servletRequestAttributes.getRequest());
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.AUTHORIZATION, buildBearer(token));

      // Retrieve user infos from OAuth provider if not already loaded
      ObjectNode user = users.computeIfAbsent(
        claims.get(SUB).toString(),
        s -> {
          ResponseEntity<ObjectNode> userInfo = restTemplate.exchange(
            registration.getProviderDetails().getUserInfoEndpoint().getUri(),
            HttpMethod.GET,
            new HttpEntity<String>(headers),
            ObjectNode.class
          );
          return userInfo.getBody();
        }
      );

      // Add custom claims
      addCustomClaim(convertedClaims, user);
    }
    return convertedClaims;
  }

  private void addCustomClaim(Map<String, Object> convertedClaims, ObjectNode user) {
    if (user == null) {
      return;
    }
    convertedClaims.put(PREFERRED_USERNAME, user.get(PREFERRED_USERNAME).asText());
    if (user.has(GIVEN_NAME)) {
      convertedClaims.put(GIVEN_NAME, user.get(GIVEN_NAME).asText());
    }
    if (user.has(FAMILY_NAME)) {
      convertedClaims.put(FAMILY_NAME, user.get(FAMILY_NAME).asText());
    }
    if (user.has(EMAIL)) {
      convertedClaims.put(EMAIL, user.get(EMAIL).asText());
    }
    // Allow full name in a name claim - happens with Auth0
    if (user.has(NAME)) {
      String[] name = user.get(NAME).asText().split("\\s+");
      if (name.length > 0) {
        convertedClaims.put(GIVEN_NAME, name[0]);
        convertedClaims.put(FAMILY_NAME, String.join(" ", Arrays.copyOfRange(name, 1, name.length)));
      }
    }
    if (user.has(GROUPS)) {
      List<String> groups = StreamSupport.stream(user.get(GROUPS).spliterator(), false).map(JsonNode::asText).toList();
      convertedClaims.put(GROUPS, groups);
    }
    if (user.has(SecurityUtils.CLAIMS_NAMESPACE + ROLES)) {
      List<String> roles = StreamSupport
        .stream(user.get(SecurityUtils.CLAIMS_NAMESPACE + ROLES).spliterator(), false)
        .map(JsonNode::asText)
        .toList();
      convertedClaims.put(ROLES, roles);
    }
  }

  private String buildBearer(String token) {
    return "Bearer " + token;
  }
}
