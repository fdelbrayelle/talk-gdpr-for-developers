package io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({ "io.github.fdelbrayelle.gdpr" })
@EnableTransactionManagement
public class DatabaseConfiguration {}
