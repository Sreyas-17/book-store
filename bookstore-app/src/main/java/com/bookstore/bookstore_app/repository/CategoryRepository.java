package com.bookstore.bookstore_app.repository;

import com.bookstore.bookstore_app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrue();
    List<Category> findByNameContainingIgnoreCase(String name);
    List<Category> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);
}
