package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Category;
import com.bookstore.bookstore_app.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LogManager.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        logger.debug("Fetching all categories");
        try {
            List<Category> categories = categoryRepository.findAll();
            logger.info("Retrieved {} categories", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("Failed to fetch all categories - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    // NEW: Get only active categories (commonly used by users)
    public List<Category> getAllActiveCategories() {
        logger.debug("Fetching all active categories");
        try {
            List<Category> categories = categoryRepository.findByIsActiveTrue();
            logger.info("Retrieved {} active categories", categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("Failed to fetch active categories - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Category getCategoryById(Long id) {
        logger.debug("Fetching category by ID: {}", id);
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });
            logger.debug("Found category: {}", category.getName());
            return category;
        } catch (Exception e) {
            logger.error("Failed to fetch category with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public Category addCategory(Category category) {
        logger.info("Adding new category: {}", category.getName());
        try {
            // Ensure new categories are active by default (already set in entity)
            Category savedCategory = categoryRepository.save(category);
            logger.info("Category added successfully with ID: {} (Active: {})",
                    savedCategory.getId(), savedCategory.isActive());
            return savedCategory;
        } catch (Exception e) {
            logger.error("Failed to add category: {} - Error: {}", category.getName(), e.getMessage(), e);
            throw e;
        }
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        logger.info("Updating category with ID: {}", id);
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found for update with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });

            // Update all fields including isActive
            category.setName(updatedCategory.getName());
            category.setDescription(updatedCategory.getDescription());
            category.setActive(updatedCategory.isActive()); // NOW USING isActive field

            Category savedCategory = categoryRepository.save(category);
            logger.info("Category updated successfully with ID: {} (Active: {})",
                    savedCategory.getId(), savedCategory.isActive());
            return savedCategory;
        } catch (Exception e) {
            logger.error("Failed to update category with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // NEW: Toggle category active status (useful for admin)
    public Category toggleCategoryStatus(Long id) {
        logger.info("Toggling category status for ID: {}", id);
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found for status toggle with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });

            boolean newStatus = !category.isActive();
            category.setActive(newStatus);
            Category savedCategory = categoryRepository.save(category);

            logger.info("Category status toggled successfully - ID: {}, New Status: {}",
                    savedCategory.getId(), newStatus ? "Active" : "Inactive");
            return savedCategory;
        } catch (Exception e) {
            logger.error("Failed to toggle category status with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // UPDATED: Soft delete - deactivate instead of hard delete
    public Category deactivateCategory(Long id) {
        logger.info("Deactivating category with ID: {}", id);
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Category not found for deactivation with ID: {}", id);
                        return new RuntimeException("Category not found");
                    });

            category.setActive(false);
            Category savedCategory = categoryRepository.save(category);
            logger.info("Category deactivated successfully with ID: {}", savedCategory.getId());
            return savedCategory;
        } catch (Exception e) {
            logger.error("Failed to deactivate category with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // Keep the hard delete method for admin use
    public void deleteCategory(Long id) {
        logger.info("Deleting category with ID: {}", id);
        try {
            if (!categoryRepository.existsById(id)) {
                logger.warn("Cannot delete - Category not found with ID: {}", id);
                throw new RuntimeException("Category not found");
            }
            categoryRepository.deleteById(id);
            logger.info("Category deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete category with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public List<Category> searchCategories(String name) {
        logger.info("Searching categories by name: {}", name);
        try {
            List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name);
            logger.info("Found {} categories matching name: {}", categories.size(), name);
            return categories;
        } catch (Exception e) {
            logger.error("Failed to search categories by name: {} - Error: {}", name, e.getMessage(), e);
            throw e;
        }
    }

    // NEW: Search only active categories
    public List<Category> searchActiveCategories(String name) {
        logger.info("Searching active categories by name: {}", name);
        try {
            // This would need a new repository method
            List<Category> categories = categoryRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
            logger.info("Found {} active categories matching name: {}", categories.size(), name);
            return categories;
        } catch (Exception e) {
            logger.error("Failed to search active categories by name: {} - Error: {}", name, e.getMessage(), e);
            // Fallback to regular search and filter
            return searchCategories(name).stream()
                    .filter(Category::isActive)
                    .toList();
        }
    }
}