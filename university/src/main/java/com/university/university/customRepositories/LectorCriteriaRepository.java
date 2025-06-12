package com.university.university.customRepositories;

import com.university.university.entities.Lector;

import java.util.List;

public interface LectorCriteriaRepository {
    List<Lector> searchByTemplate(String template);
}