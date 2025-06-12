package com.university.university.customRepositories;

import com.university.university.DTO.DepartmentStatisticsDto;
import com.university.university.entities.Degree;
import com.university.university.entities.Department;
import com.university.university.entities.Lector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.util.*;

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
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Department> department = query.from(Department.class);
        Join<Object, Object> lector = department.join("lectors");

        query.multiselect(
                cb.sum(cb.<Long>selectCase()
                        .when(cb.equal(lector.get("degree"), Degree.ASSISTANT), 1L)
                        .otherwise(0L)).alias("assistants"),
                cb.sum(cb.<Long>selectCase()
                        .when(cb.equal(lector.get("degree"), Degree.ASSOCIATE_PROFESSOR), 1L)
                        .otherwise(0L)).alias("associateProfessors"),
                cb.sum(cb.<Long>selectCase()
                        .when(cb.equal(lector.get("degree"), Degree.PROFESSOR), 1L)
                        .otherwise(0L)).alias("professors")
        ).where(cb.equal(department.get("departmentName"), departmentName));

        Tuple result = entityManager.createQuery(query).getSingleResult();
        return new DepartmentStatisticsDto(
                ((Number) result.get("assistants")).longValue(),
                ((Number) result.get("associateProfessors")).longValue(),
                ((Number) result.get("professors")).longValue()
        );
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
