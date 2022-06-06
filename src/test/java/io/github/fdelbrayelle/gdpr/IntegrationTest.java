package io.github.fdelbrayelle.gdpr;

import io.github.fdelbrayelle.gdpr.security.oauth2.infrastructure.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@DisplayNameGeneration(ReplaceCamelCase.class)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Gdpr_for_developersApp.class })
@Target(ElementType.TYPE)
@WithMockUser
public @interface IntegrationTest {
  public String[] properties() default {};
}
