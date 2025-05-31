package com.japanese.study_app.repository;

import com.japanese.study_app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Interacts directly with the database, provides CRUD operation and
    // additional query methods
    // Custom queries can be defined here
    // Inherits methods for CRUD such as save, findById, findAll, deleteById
    Optional<Category> findByName(String name);

    boolean existsByName(String name);
}
