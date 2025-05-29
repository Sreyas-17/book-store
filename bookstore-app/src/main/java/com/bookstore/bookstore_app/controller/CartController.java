package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.CartItem;
import com.bookstore.bookstore_app.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*") // Allow requests from any origin
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    /**
     * Adds a book to the user's cart.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") int quantity) {
        logger.info("POST /api/cart/add - User ID: {}, Book ID: {}, Quantity: {}", userId, bookId, quantity);

        try {
            CartItem cartItem = cartService.addToCart(userId, bookId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartItem));
        } catch (Exception e) {
            logger.error("Error adding to cart - User ID: {}, Book ID: {} - {}", userId, bookId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Removes a book from the user's cart.
     */
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        logger.info("DELETE /api/cart/remove - User ID: {}, Book ID: {}", userId, bookId);

        try {
            cartService.removeFromCart(userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
        } catch (Exception e) {
            logger.error("Error removing from cart - User ID: {}, Book ID: {} - {}", userId, bookId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Updates the quantity of a book in the user's cart.
     */
    @PutMapping("/update-quantity")
    public ResponseEntity<ApiResponse<CartItem>> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam int quantity) {
        logger.info("PUT /api/cart/update-quantity - User ID: {}, Book ID: {}, Quantity: {}", userId, bookId, quantity);

        try {
            CartItem cartItem = cartService.updateQuantity(userId, bookId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Quantity updated", cartItem));
        } catch (Exception e) {
            logger.error("Error updating cart quantity - User ID: {}, Book ID: {} - {}", userId, bookId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves all cart items for a given user.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        logger.info("GET /api/cart/{} - Fetching cart items", userId);

        try {
            List<CartItem> cartItems = cartService.getCartItems(userId);
            return ResponseEntity.ok(ApiResponse.success("Cart items retrieved", cartItems));
        } catch (Exception e) {
            logger.error("Error fetching cart items - User ID: {} - {}", userId, e.getMessage());
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to retrieve cart items: " + e.getMessage()));
        }
    }

    /**
     * Calculates the total price of items in the user's cart.
     */
    @GetMapping("/{userId}/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getCartTotal(@PathVariable Long userId) {
        logger.info("GET /api/cart/{}/total - Calculating cart total", userId);

        try {
            BigDecimal total = cartService.getCartTotal(userId);
            return ResponseEntity.ok(ApiResponse.success("Cart total calculated", total));
        } catch (Exception e) {
            logger.error("Error calculating cart total - User ID: {} - {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Clears all items from the user's cart.
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable Long userId) {
        logger.info("DELETE /api/cart/{}/clear - Clearing cart", userId);

        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
        } catch (Exception e) {
            logger.error("Error clearing cart - User ID: {} - {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
