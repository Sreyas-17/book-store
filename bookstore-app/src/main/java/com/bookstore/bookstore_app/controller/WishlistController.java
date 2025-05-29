package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Wishlist;
import com.bookstore.bookstore_app.service.WishlistService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    private static final Logger logger = LogManager.getLogger(WishlistController.class);

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Wishlist>> addToWishlist(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        logger.info("POST /api/wishlist/add - Adding to wishlist - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            Wishlist wishlist = wishlistService.addToWishlist(userId, bookId);
            logger.info("Book added to wishlist successfully via API - Wishlist ID: {}", wishlist.getId());
            return ResponseEntity.ok(ApiResponse.success("Book added to wishlist", wishlist));
        } catch (Exception e) {
            logger.error("Error adding to wishlist via API - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> removeFromWishlist(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        logger.info("DELETE /api/wishlist/remove - Removing from wishlist - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            wishlistService.removeFromWishlist(userId, bookId);
            logger.info("Book removed from wishlist successfully via API - User ID: {}, Book ID: {}", userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Book removed from wishlist"));
        } catch (Exception e) {
            logger.error("Error removing from wishlist via API - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Wishlist>>> getUserWishlist(@PathVariable Long userId) {
        logger.info("GET /api/wishlist/{} - Fetching user wishlist", userId);
        
        try {
            List<Wishlist> wishlist = wishlistService.getUserWishlist(userId);
            logger.info("Retrieved {} wishlist items via API for user ID: {}", wishlist.size(), userId);
            return ResponseEntity.ok(ApiResponse.success("Wishlist retrieved", wishlist));
        } catch (Exception e) {
            logger.error("Error fetching wishlist via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/move-to-cart")
    public ResponseEntity<ApiResponse<String>> moveToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        logger.info("POST /api/wishlist/move-to-cart - Moving to cart - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            wishlistService.moveToCart(userId, bookId);
            logger.info("Book moved from wishlist to cart successfully via API - User ID: {}, Book ID: {}", userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Book moved to cart"));
        } catch (Exception e) {
            logger.error("Error moving book to cart via API - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}