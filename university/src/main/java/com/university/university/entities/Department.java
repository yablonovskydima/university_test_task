package com.university.university.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(
        name = "departments",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "departmentName", name = "uq_departments_departmentName")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "department_name", unique = true)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "head_of_department_id")
    private Lector headOfDepartment;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "departments_lectors",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "lector_id")
    )
    private Set<Lector> lectors;

}
