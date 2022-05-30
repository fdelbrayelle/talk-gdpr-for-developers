--liquibase formatted sql

--changeset fdelbrayelle:0001_init_db_a_address
CREATE TABLE IF NOT EXISTS address (
  id UUID PRIMARY KEY,
  address_number INTEGER NOT NULL,
  street VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  zip_code VARCHAR(255) NOT NULL,
  country VARCHAR(255) NOT NULL
  );
--rollback DROP TABLE address

--changeset fdelbrayelle:0001_init_db_b_users
CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  lastname VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  phone_number VARCHAR(255) NOT NULL,
  sex VARCHAR(255) NOT NULL,
  address_id UUID NOT NULL,
  FOREIGN KEY (address_id) REFERENCES address (id)
);
--rollback DROP TABLE users
