package com.university.university.repositories;

import com.university.university.customRepositories.DepartmentCriteriaRepository;
import com.university.university.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, DepartmentCriteriaRepository {
    Optional<Department> findByDepartmentName(String departmentName);
}
