package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Category;
import com.bookstore.bookstore_app.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private static final Logger logger = LogManager.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        logger.info("GET /api/categories - Fetching all categories");

        try {
            List<Category> categories = categoryService.getAllCategories();
            logger.info("Retrieved {} categories via API", categories.size());
            return ResponseEntity.ok(ApiResponse.success("Categories retrieved", categories));
        } catch (Exception e) {
            logger.error("Error fetching categories via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Category>>> getActiveCategories() {
        logger.info("GET /api/categories/active - Fetching active categories");

        try {
            List<Category> categories = categoryService.getAllActiveCategories();
            logger.info("Retrieved {} active categories via API", categories.size());
            return ResponseEntity.ok(ApiResponse.success("Active categories retrieved", categories));
        } catch (Exception e) {
            logger.error("Error fetching active categories via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addCategory(@RequestBody Category category) {
        logger.info("POST /api/categories - Adding new category: {}", category.getName());

        try {
            Category savedCategory = categoryService.addCategory(category);
            logger.info("Category added successfully via API - ID: {}", savedCategory.getId());
            return ResponseEntity.ok(ApiResponse.success("Category added", savedCategory));
        } catch (Exception e) {
            logger.error("Error adding category via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        logger.info("PUT /api/categories/{} - Updating category", id);

        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            logger.info("Category updated successfully via API - ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success("Category updated", updatedCategory));
        } catch (Exception e) {
            logger.error("Error updating category via API - ID: {} - Error: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        logger.info("DELETE /api/categories/{} - Deleting category", id);

        try {
            categoryService.deleteCategory(id);
            logger.info("Category deleted successfully via API - ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success("Category deleted"));
        } catch (Exception e) {
            logger.error("Error deleting category via API - ID: {} - Error: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
