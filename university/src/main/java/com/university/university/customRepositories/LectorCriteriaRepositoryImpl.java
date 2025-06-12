package com.university.university.customRepositories;

import com.university.university.entities.Lector;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectorCriteriaRepositoryImpl implements LectorCriteriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Lector> searchByTemplate(String template) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Lector> query = cb.createQuery(Lector.class);
        Root<Lector> root = query.from(Lector.class);

        String likePattern = "%" + template.toLowerCase() + "%";

        Predicate firstNameLike = cb.like(cb.lower(root.get("firstname")), likePattern);
        Predicate lastNameLike = cb.like(cb.lower(root.get("lastname")), likePattern);

        query.select(root)
             .where(cb.or(firstNameLike, lastNameLike));

        return entityManager.createQuery(query).getResultList();
    }
}