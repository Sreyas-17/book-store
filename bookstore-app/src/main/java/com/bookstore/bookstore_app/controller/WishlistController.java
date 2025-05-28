package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Wishlist;
import com.bookstore.bookstore_app.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Wishlist>> addToWishlist(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        try {
            Wishlist wishlist = wishlistService.addToWishlist(userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Book added to wishlist", wishlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> removeFromWishlist(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        try {
            wishlistService.removeFromWishlist(userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Book removed from wishlist"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<Wishlist>>> getUserWishlist(@PathVariable Long userId) {
        try {
            List<Wishlist> wishlist = wishlistService.getUserWishlist(userId);
            return ResponseEntity.ok(ApiResponse.success("Wishlist retrieved", wishlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/move-to-cart")
    public ResponseEntity<ApiResponse<String>> moveToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        try {
            wishlistService.moveToCart(userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Book moved to cart"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}