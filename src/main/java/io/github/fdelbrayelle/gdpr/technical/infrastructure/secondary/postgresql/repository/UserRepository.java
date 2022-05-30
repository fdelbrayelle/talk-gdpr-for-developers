package io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.repository;

import io.github.fdelbrayelle.gdpr.technical.infrastructure.secondary.postgresql.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {}
