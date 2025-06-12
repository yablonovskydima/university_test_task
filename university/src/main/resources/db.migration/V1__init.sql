CREATE DATABASE IF NOT EXISTS university_db;
USE finance_manager_db;

CREATE TABLE IF NOT EXISTS lectors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    degree ENUM('ASSISTANT', 'ASSOCIATE_PROFESSOR', 'PROFESSOR') NOT NULL,
    CONSTRAINT pk_lectors_id PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_departments_id PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS departments_lectors (
    department_id BIGINT NOT NULL,
    lector_id BIGINT NOT NULL,
    CONSTRAINT pk_departments_lectors_department_id_lector_id PRIMARY KEY (department_id, lector_id),
    CONSTRAINT fk_departments_lectors_department_id FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE CASCADE,
    CONSTRAINT fk_departments_lectors_lector_id FOREIGN KEY (lector_id) REFERENCES lector(id) ON DELETE CASCADE
)ENGINE=InnoDB;
