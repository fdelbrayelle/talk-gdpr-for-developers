package io.github.fdelbrayelle.gdpr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayNameGeneration(ReplaceCamelCase.class)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Gdpr_for_developersApp.class })
@Target(ElementType.TYPE)
public @interface IntegrationTest {
  public String[] properties() default {};
}
