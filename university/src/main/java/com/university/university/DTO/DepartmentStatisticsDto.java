package com.university.university.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStatisticsDto {
    private Long assistants;
    private Long associateProfessors;
    private Long professors;
}