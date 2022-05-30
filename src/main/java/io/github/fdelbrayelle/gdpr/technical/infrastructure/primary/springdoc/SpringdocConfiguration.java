package io.github.fdelbrayelle.gdpr.technical.infrastructure.primary.springdoc;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfiguration {

  @Value("${application.version:undefined}")
  private String version;

  @Bean
  public OpenAPI gdprOpenAPI() {
    return new OpenAPI()
      .info(
        new Info()
          .title("Project API")
          .description("Project description API")
          .version(version)
          .license(new License().name("No license").url(""))
      )
      .externalDocs(new ExternalDocumentation().description("Project Documentation").url(""));
  }

  @Bean
  public GroupedOpenApi gdprAllOpenAPI() {
    // prettier-ignore
    return GroupedOpenApi.builder()
      .group("all")
      .pathsToMatch("/api/**")
      .build();
  }
}
