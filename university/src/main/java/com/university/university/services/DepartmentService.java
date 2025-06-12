package com.university.university.services;

import com.university.university.DTO.DepartmentDto;
import com.university.university.DTO.DepartmentStatisticsDto;
import com.university.university.entities.Department;
import com.university.university.entities.Lector;
import com.university.university.exceptions.ResourceNotFoundException;
import com.university.university.mappers.GenericMapper;
import com.university.university.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<DepartmentDto> getAll() {
        return GenericMapper.mapList(departmentRepository.findAll(), DepartmentDto.class);
    }

    public DepartmentDto getById(Long id) {
        return GenericMapper.map(findByIdOrThrow(id), DepartmentDto.class);
    }

    @Transactional
    public DepartmentDto create(Department department) {
        return GenericMapper.map(departmentRepository.save(department), DepartmentDto.class);
    }

    @Transactional
    public DepartmentDto update(Long id, Department updatedDepartment) {
        Department existing = findByIdOrThrow(id);
        GenericMapper.updateMap(updatedDepartment, existing);
        return GenericMapper.map(departmentRepository.save(existing), DepartmentDto.class);
    }

    @Transactional
    public void deleteById(Long id) {
        departmentRepository.delete(findByIdOrThrow(id));
    }
    public String getHeadOfDepartment(String departmentName) {
        Department department = departmentRepository.findByDepartmentName(departmentName)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Lector head = department.getHeadOfDepartment();

        if (Objects.isNull(head)) {
            return "Head of " + departmentName + " department is not assigned";
        }

        return head.getFirstname() + " " + head.getLastname();
    }
    public DepartmentStatisticsDto getStatistics(String departmentName) {
        return departmentRepository.getDegreeStatsByDepartmentName(departmentName);
    }
    public Double getAverageSalary(String departmentName) {
        return departmentRepository.findAverageSalaryByDepartmentName(departmentName).orElseThrow();
    }
    public Long countEmployees(String departmentName) {
        return departmentRepository.countEmployeesByDepartmentName(departmentName);
    }
    private Department findByIdOrThrow(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department is not found"));
    }
}
