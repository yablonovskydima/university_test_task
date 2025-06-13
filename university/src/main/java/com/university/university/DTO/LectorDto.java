package com.university.university.DTO;

import com.university.university.entities.Degree;
import lombok.Data;

@Data
public class LectorDto {
    private Long id;
    private String firstname;
    private String lastname;
    private Degree degree;
    private Double salary;
}
