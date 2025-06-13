package com.university.university.customRepositories;

import com.university.university.DTO.DepartmentStatisticsDto;

import java.util.Optional;

public interface DepartmentCriteriaRepository {
    Optional<Double> findAverageSalaryByDepartmentName(String departmentName);
    DepartmentStatisticsDto getDegreeStatsByDepartmentName(String departmentName);
    Long countEmployeesByDepartmentName(String departmentName);

}