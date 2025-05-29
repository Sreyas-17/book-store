package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.config.JwtAuthenticationToken;
import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.dto.VendorRegistrationRequest;
import com.bookstore.bookstore_app.dto.BookCreateRequest;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.service.VendorService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins = "*")
public class VendorController {

    private static final Logger logger = LogManager.getLogger(VendorController.class);

    @Autowired
    private VendorService vendorService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Vendor>> registerVendor(
            @Valid @RequestBody VendorRegistrationRequest request,
            Authentication authentication) {
        
        logger.info("POST /api/vendor/register - Vendor registration request");
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            Vendor vendor = vendorService.registerVendor(userId, request);
            
            logger.info("Vendor registration successful - User ID: {}, Business: {}", 
                       userId, vendor.getBusinessName());
            
            return ResponseEntity.ok(ApiResponse.success(
                "Vendor registration successful. Awaiting admin approval.", vendor));
                
        } catch (Exception e) {
            logger.error("Error in vendor registration - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Vendor>> getVendorProfile(Authentication authentication) {
        logger.info("GET /api/vendor/profile - Fetching vendor profile");
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            Vendor vendor = vendorService.getVendorProfile(userId);
            
            logger.info("Vendor profile retrieved - Business: {}", vendor.getBusinessName());
            return ResponseEntity.ok(ApiResponse.success("Vendor profile retrieved", vendor));
            
        } catch (Exception e) {
            logger.error("Error fetching vendor profile - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Vendor>> updateVendorProfile(
            @Valid @RequestBody VendorRegistrationRequest request,
            Authentication authentication) {
        
        logger.info("PUT /api/vendor/profile - Updating vendor profile");
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            Vendor vendor = vendorService.updateVendorProfile(userId, request);
            
            logger.info("Vendor profile updated - Business: {}", vendor.getBusinessName());
            return ResponseEntity.ok(ApiResponse.success("Vendor profile updated", vendor));
            
        } catch (Exception e) {
            logger.error("Error updating vendor profile - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/books")
    public ResponseEntity<ApiResponse<Book>> addBook(
            @Valid @RequestBody BookCreateRequest request,
            Authentication authentication) {
        
        logger.info("POST /api/vendor/books - Adding new book: {}", request.getTitle());
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            Book book = vendorService.addBook(userId, request);
            
            logger.info("Book added successfully - ID: {}, Title: {}", book.getId(), book.getTitle());
            return ResponseEntity.ok(ApiResponse.success(
                "Book added successfully. Awaiting admin approval.", book));
                
        } catch (Exception e) {
            logger.error("Error adding book - Title: {} - Error: {}", request.getTitle(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<List<Book>>> getVendorBooks(Authentication authentication) {
        logger.info("GET /api/vendor/books - Fetching vendor's books");
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            List<Book> books = vendorService.getVendorBooks(userId);
            
            logger.info("Retrieved {} books for vendor", books.size());
            return ResponseEntity.ok(ApiResponse.success("Vendor books retrieved", books));
            
        } catch (Exception e) {
            logger.error("Error fetching vendor books - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<Book>> updateBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BookCreateRequest request,
            Authentication authentication) {
        
        logger.info("PUT /api/vendor/books/{} - Updating book", bookId);
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            Book book = vendorService.updateBook(userId, bookId, request);
            
            logger.info("Book updated successfully - ID: {}, Title: {}", book.getId(), book.getTitle());
            return ResponseEntity.ok(ApiResponse.success(
                "Book updated successfully. Awaiting admin re-approval.", book));
                
        } catch (Exception e) {
            logger.error("Error updating book ID: {} - Error: {}", bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<ApiResponse<String>> deleteBook(
            @PathVariable Long bookId,
            Authentication authentication) {
        
        logger.info("DELETE /api/vendor/books/{} - Deleting book", bookId);
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            vendorService.deleteBook(userId, bookId);
            
            logger.info("Book deleted successfully - ID: {}", bookId);
            return ResponseEntity.ok(ApiResponse.success("Book deleted successfully"));
            
        } catch (Exception e) {
            logger.error("Error deleting book ID: {} - Error: {}", bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getVendorDashboard(Authentication authentication) {
        logger.info("GET /api/vendor/dashboard - Fetching vendor dashboard");
        
        try {
            Long userId = getUserIdFromAuth(authentication);
            Map<String, Object> dashboard = vendorService.getVendorDashboard(userId);
            
            logger.info("Vendor dashboard retrieved successfully");
            return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved", dashboard));
            
        } catch (Exception e) {
            logger.error("Error fetching vendor dashboard - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Helper method to extract user ID from JWT authentication
    private Long getUserIdFromAuth(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            return jwtAuth.getUserId();
        }
        throw new RuntimeException("Invalid authentication token");
    }
}