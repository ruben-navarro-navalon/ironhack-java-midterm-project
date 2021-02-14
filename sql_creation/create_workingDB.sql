CREATE SCHEMA midterm_project;
USE midterm_project;

CREATE TABLE user (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  username varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (username)
);

CREATE TABLE role (
  id bigint NOT NULL AUTO_INCREMENT,
  user_role varchar(255) DEFAULT NULL,
  user_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE admin (
  id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES user (id)
);

CREATE TABLE third_party (
  id bigint NOT NULL AUTO_INCREMENT,
  hash_key varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE account_holder (
  birth_date date DEFAULT NULL,
  mailing_address_city varchar(255) DEFAULT NULL,
  mailing_address_direction varchar(255) DEFAULT NULL,
  mailing_address_postal_code int DEFAULT NULL,
  mailing_address_state varchar(255) DEFAULT NULL,
  primary_address_city varchar(255) DEFAULT NULL,
  primary_address_direction varchar(255) DEFAULT NULL,
  primary_address_postal_code int DEFAULT NULL,
  primary_address_state varchar(255) DEFAULT NULL,
  id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES user (id)
);

CREATE TABLE account (
  id bigint NOT NULL AUTO_INCREMENT,
  balance_amount decimal(19,2) DEFAULT NULL,
  balance_currency varchar(255) DEFAULT NULL,
  creation_date date DEFAULT NULL,
  penalty_amount decimal(19,2) DEFAULT NULL,
  penalty_currency varchar(255) DEFAULT NULL,
  primary_owner_id bigint DEFAULT NULL,
  secondary_owner_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (secondary_owner_id) REFERENCES account_holder (id),
  FOREIGN KEY (primary_owner_id) REFERENCES account_holder (id)
);

CREATE TABLE credit_card (
  credit_limit_amount decimal(19,2) DEFAULT NULL,
  credit_limit_currency varchar(255) DEFAULT NULL,
  interest_rate decimal(5,4) DEFAULT NULL,
  interest_update date DEFAULT NULL,
  id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES account (id)
);

CREATE TABLE savings (
  below_minimum_balance bit(1) DEFAULT NULL,
  interest_rate decimal(5,4) DEFAULT NULL,
  interest_update date DEFAULT NULL,
  minimum_balance_amount decimal(19,2) DEFAULT NULL,
  minimum_balance_currency varchar(255) DEFAULT NULL,
  secret_key varchar(255) DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES account (id)
);

CREATE TABLE student_checking (
  secret_key varchar(255) DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES account (id)
);

CREATE TABLE checking (
  below_minimum_balance bit(1) DEFAULT NULL,
  maintenance_update date DEFAULT NULL,
  minimum_balance_amount decimal(19,2) DEFAULT NULL,
  minimum_balance_currency varchar(255) DEFAULT NULL,
  monthly_maintenance_fee_amount decimal(19,2) DEFAULT NULL,
  monthly_maintenance_fee_currency varchar(255) DEFAULT NULL,
  secret_key varchar(255) DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES account (id)
);

CREATE TABLE transfer (
  id bigint NOT NULL AUTO_INCREMENT,
  amount decimal(19,2) DEFAULT NULL,
  currency varchar(255) DEFAULT NULL,
  concept varchar(255) DEFAULT NULL,
  date datetime(6) DEFAULT NULL,
  destination_name varchar(255) DEFAULT NULL,
  destination_account_id bigint DEFAULT NULL,
  origin_account_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (destination_account_id) REFERENCES account (id),
  FOREIGN KEY (origin_account_id) REFERENCES account (id)
);

-- Pre-defined default admin username/password is admin/1234 (the password is hashed in the DB). Use it for testing purposes.
INSERT INTO user (id, name, password, username) VALUES (1, "Admin Name", "$2a$10$RFb9IXjhOrYv2OrjE5ru0OOv65OxN7NXB83abFOMfqENWw4Id.k3q", "admin");
INSERT INTO role (user_role, user_id) VALUES ("ADMIN", 1);
INSERT INTO admin (id) VALUES (1);