package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Review;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/books/{bookId}/approve")
    public ResponseEntity<ApiResponse<Book>> approveBook(@PathVariable Long bookId) {
        try {
            Book book = adminService.approveBook(bookId);
            return ResponseEntity.ok(ApiResponse.success("Book approved", book));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/books/{bookId}/disapprove")
    public ResponseEntity<ApiResponse<Book>> disapproveBook(@PathVariable Long bookId) {
        try {
            Book book = adminService.disapproveBook(bookId);
            return ResponseEntity.ok(ApiResponse.success("Book disapproved", book));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/reviews/{reviewId}/approve")
    public ResponseEntity<ApiResponse<Review>> approveReview(@PathVariable Long reviewId) {
        try {
            Review review = adminService.approveReview(reviewId);
            return ResponseEntity.ok(ApiResponse.success("Review approved", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/books/pending")
    public ResponseEntity<ApiResponse<List<Book>>> getPendingBooks() {
        try {
            List<Book> books = adminService.getPendingBooks();
            return ResponseEntity.ok(ApiResponse.success("Pending books retrieved", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/vendors/all")
    public ResponseEntity<ApiResponse<List<Vendor>>> getAllVendors() {
        try {
            List<Vendor> vendors = adminService.getAllVendors();
            return ResponseEntity.ok(ApiResponse.success("Vendors retrieved", vendors));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
