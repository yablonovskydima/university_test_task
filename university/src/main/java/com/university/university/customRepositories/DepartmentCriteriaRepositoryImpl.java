package com.university.university.customRepositories;

import com.university.university.DTO.DepartmentStatisticsDto;
import com.university.university.entities.Degree;
import com.university.university.entities.Department;
import com.university.university.entities.Lector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class DepartmentCriteriaRepositoryImpl implements DepartmentCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Double> findAverageSalaryByDepartmentName(String departmentName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = cb.createQuery(Double.class);
        Root<Department> root = query.from(Department.class);
        Join<Department, Lector> join = root.join("lectors");

        query.select(cb.avg(join.get("salary")))
                .where(cb.equal(root.get("departmentName"), departmentName));

        Double result = entityManager.createQuery(query).getSingleResult();
        return Optional.ofNullable(result);
    }

    @Override
    public DepartmentStatisticsDto getDegreeStatsByDepartmentName(String departmentName) {
        Long assistants = countByDegree(departmentName, Degree.ASSISTANT);
        Long associateProfessors = countByDegree(departmentName, Degree.ASSOCIATE_PROFESSOR);
        Long professors = countByDegree(departmentName, Degree.PROFESSOR);

        return new DepartmentStatisticsDto(assistants, associateProfessors, professors);
    }

    private Long countByDegree(String departmentName, Degree degree) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Department> department = query.from(Department.class);
        Join<Department, Lector> lector = department.join("lectors", JoinType.LEFT);

        query.select(cb.count(lector))
                .where(
                        cb.and(
                                cb.equal(department.get("departmentName"), departmentName),
                                cb.equal(lector.get("degree"), degree)
                        )
                );

        return entityManager.createQuery(query).getSingleResult();
    }




    @Override
    public Long countEmployeesByDepartmentName(String departmentName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Department> department = query.from(Department.class);
        Join<Department, Lector> lectors = department.join("lectors", JoinType.LEFT);

        query.select(cb.countDistinct(lectors))
                .where(cb.equal(department.get("departmentName"), departmentName));

        return entityManager.createQuery(query).getSingleResult();
    }

}
