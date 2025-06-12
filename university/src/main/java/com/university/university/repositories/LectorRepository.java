package com.university.university.repositories;

import com.university.university.customRepositories.LectorCriteriaRepository;
import com.university.university.entities.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long>, LectorCriteriaRepository {
}
