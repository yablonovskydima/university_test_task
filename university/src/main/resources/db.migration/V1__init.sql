CREATE DATABASE IF NOT EXISTS university_db;
USE university_db;

CREATE TABLE IF NOT EXISTS lectors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    degree ENUM('ASSISTANT', 'ASSOCIATE_PROFESSOR', 'PROFESSOR') NOT NULL,
    salary DOUBLE NOT NULL,
    CONSTRAINT pk_lectors_id PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS departments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    department_name VARCHAR(255) NOT NULL,
    head_of_department_id BIGINT,
    CONSTRAINT pk_departments_id PRIMARY KEY(id),
    CONSTRAINT uq_departments_department_name UNIQUE(department_name),
    CONSTRAINT fk_head_of_department FOREIGN KEY (head_of_department_id) REFERENCES lectors(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS departments_lectors (
    department_id BIGINT NOT NULL,
    lector_id BIGINT NOT NULL,
    CONSTRAINT pk_departments_lectors PRIMARY KEY (department_id, lector_id),
    CONSTRAINT fk_departments_lectors_department FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE,
    CONSTRAINT fk_departments_lectors_lector FOREIGN KEY (lector_id) REFERENCES lectors(id) ON DELETE CASCADE
) ENGINE=InnoDB;
