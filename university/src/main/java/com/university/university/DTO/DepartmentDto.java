package com.university.university.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDto {
    private Long id;
    private String departmentName;
    private LectorDto headOfDepartment;
    private List<LectorDto> lectors;
}
