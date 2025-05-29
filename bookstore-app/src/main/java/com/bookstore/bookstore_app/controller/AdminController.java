package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Review;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}