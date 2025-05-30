package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.*;
import com.bookstore.bookstore_app.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @PutMapping("/books/{bookId}/approve")
    public ResponseEntity<ApiResponse<Book>> approveBook(@PathVariable Long bookId) {
        logger.info("PUT /api/admin/books/{}/approve - Approving book", bookId);

        try {
            Book book = adminService.approveBook(bookId);
            logger.info("Book approved successfully via API - Book ID: {}, Title: {}", bookId, book.getTitle());
            return ResponseEntity.ok(ApiResponse.success("Book approved", book));
        } catch (Exception e) {
            logger.error("Error approving book via API - Book ID: {} - Error: {}", bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/books/{bookId}/disapprove")
    public ResponseEntity<ApiResponse<Book>> disapproveBook(@PathVariable Long bookId) {
        logger.info("PUT /api/admin/books/{}/disapprove - Disapproving book", bookId);

        try {
            Book book = adminService.disapproveBook(bookId);
            logger.info("Book disapproved successfully via API - Book ID: {}, Title: {}", bookId, book.getTitle());
            return ResponseEntity.ok(ApiResponse.success("Book disapproved", book));
        } catch (Exception e) {
            logger.error("Error disapproving book via API - Book ID: {} - Error: {}", bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/reviews/{reviewId}/approve")
    public ResponseEntity<ApiResponse<Review>> approveReview(@PathVariable Long reviewId) {
        logger.info("PUT /api/admin/reviews/{}/approve - Approving review", reviewId);

        try {
            Review review = adminService.approveReview(reviewId);
            logger.info("Review approved successfully via API - Review ID: {}", reviewId);
            return ResponseEntity.ok(ApiResponse.success("Review approved", review));
        } catch (Exception e) {
            logger.error("Error approving review via API - Review ID: {} - Error: {}", reviewId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/books/pending")
    public ResponseEntity<ApiResponse<List<Book>>> getPendingBooks() {
        logger.info("GET /api/admin/books/pending - Fetching pending books");

        try {
            List<Book> books = adminService.getPendingBooks();
            logger.info("Retrieved {} pending books via API", books.size());
            return ResponseEntity.ok(ApiResponse.success("Pending books retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching pending books via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/books/all")
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        logger.info("GET /api/admin/books/all - Admin fetching all books");

        try {
            List<Book> books = adminService.getAllBooks();
            logger.info("Admin retrieved {} books via API", books.size());
            return ResponseEntity.ok(ApiResponse.success("All books retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching all books for admin via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/vendors/all")
    public ResponseEntity<ApiResponse<List<Vendor>>> getAllVendors() {
        logger.info("GET /api/admin/vendors/all - Fetching all vendors");

        try {
            List<Vendor> vendors = adminService.getAllVendors();
            logger.info("Retrieved {} vendors via API", vendors.size());
            return ResponseEntity.ok(ApiResponse.success("Vendors retrieved", vendors));
        } catch (Exception e) {
            logger.error("Error fetching vendors via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Add these methods to your existing AdminController.java

    @PutMapping("/vendors/{vendorId}/approve")
    public ResponseEntity<ApiResponse<Vendor>> approveVendor(@PathVariable Long vendorId) {
        logger.info("PUT /api/admin/vendors/{}/approve - Approving vendor", vendorId);

        try {
            Vendor vendor = adminService.approveVendor(vendorId);
            logger.info("Vendor approved successfully via API - Vendor ID: {}, Business: {}",
                    vendorId, vendor.getBusinessName());
            return ResponseEntity.ok(ApiResponse.success("Vendor approved", vendor));
        } catch (Exception e) {
            logger.error("Error approving vendor via API - Vendor ID: {} - Error: {}", vendorId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/vendors/{vendorId}/disapprove")
    public ResponseEntity<ApiResponse<Vendor>> disapproveVendor(@PathVariable Long vendorId) {
        logger.info("PUT /api/admin/vendors/{}/disapprove - Disapproving vendor", vendorId);

        try {
            Vendor vendor = adminService.disapproveVendor(vendorId);
            logger.info("Vendor disapproved successfully via API - Vendor ID: {}, Business: {}",
                    vendorId, vendor.getBusinessName());
            return ResponseEntity.ok(ApiResponse.success("Vendor disapproved", vendor));
        } catch (Exception e) {
            logger.error("Error disapproving vendor via API - Vendor ID: {} - Error: {}", vendorId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/vendors/pending")
    public ResponseEntity<ApiResponse<List<Vendor>>> getPendingVendors() {
        logger.info("GET /api/admin/vendors/pending - Fetching pending vendors");

        try {
            List<Vendor> vendors = adminService.getPendingVendors();
            logger.info("Retrieved {} pending vendors via API", vendors.size());
            return ResponseEntity.ok(ApiResponse.success("Pending vendors retrieved", vendors));
        } catch (Exception e) {
            logger.error("Error fetching pending vendors via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAdminDashboard() {
        logger.info("GET /api/admin/dashboard - Fetching admin dashboard");

        try {
            Map<String, Object> dashboard = adminService.getAdminDashboard();
            logger.info("Admin dashboard retrieved successfully");
            return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved", dashboard));
        } catch (Exception e) {
            logger.error("Error fetching admin dashboard via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        logger.info("GET /api/admin/users - Fetching all users");

        try {
            List<User> users = adminService.getAllUsers();
            logger.info("Retrieved {} users via API", users.size());
            return ResponseEntity.ok(ApiResponse.success("Users retrieved", users));
        } catch (Exception e) {
            logger.error("Error fetching users via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        logger.info("GET /api/admin/orders - Fetching all orders");

        try {
            List<Order> orders = adminService.getAllOrders();
            logger.info("Retrieved {} orders via API", orders.size());
            return ResponseEntity.ok(ApiResponse.success("Orders retrieved", orders));
        } catch (Exception e) {
            logger.error("Error fetching orders via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<ApiResponse<User>> updateUserRole(
            @PathVariable Long userId,
            @RequestParam User.Role role) {

        logger.info("PUT /api/admin/users/{}/role - Updating user role to: {}", userId, role);

        try {
            User user = adminService.updateUserRole(userId, role);
            logger.info("User role updated - User ID: {}, New Role: {}", userId, role);
            return ResponseEntity.ok(ApiResponse.success("User role updated", user));
        } catch (Exception e) {
            logger.error("Error updating user role - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}