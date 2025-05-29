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
@CrossOrigin(origins = "*")
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") int quantity) {
        logger.info("POST /api/cart/add - User ID: {}, Book ID: {}, Quantity: {}", userId, bookId, quantity);
        
        try {
            CartItem cartItem = cartService.addToCart(userId, bookId, quantity);
            logger.info("Item added to cart successfully via API - Cart Item ID: {}", cartItem.getId());
            return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartItem));
        } catch (Exception e) {
            logger.error("Error adding to cart via API - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        logger.info("DELETE /api/cart/remove - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            cartService.removeFromCart(userId, bookId);
            logger.info("Item removed from cart successfully via API - User ID: {}, Book ID: {}", userId, bookId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
        } catch (Exception e) {
            logger.error("Error removing from cart via API - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<ApiResponse<CartItem>> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam int quantity) {
        logger.info("PUT /api/cart/update-quantity - User ID: {}, Book ID: {}, Quantity: {}", userId, bookId, quantity);
        
        try {
            CartItem cartItem = cartService.updateQuantity(userId, bookId, quantity);
            logger.info("Cart quantity updated successfully via API");
            return ResponseEntity.ok(ApiResponse.success("Quantity updated", cartItem));
        } catch (Exception e) {
            logger.error("Error updating cart quantity via API - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        logger.info("GET /api/cart/{} - Fetching cart items", userId);
        
        try {
            List<CartItem> cartItems = cartService.getCartItems(userId);
            logger.info("Retrieved {} cart items via API for user ID: {}", cartItems.size(), userId);

            // Debug log for cart items
            if (logger.isDebugEnabled()) {
                for (CartItem item : cartItems) {
                    logger.debug("Cart Item - ID: {}, Book: {}, Quantity: {}", 
                                item.getId(), 
                                item.getBook() != null ? item.getBook().getTitle() : "null", 
                                item.getQuantity());
                }
            }

            return ResponseEntity.ok(ApiResponse.success("Cart items retrieved", cartItems));
        } catch (Exception e) {
            logger.error("Error fetching cart items via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to retrieve cart items: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getCartTotal(@PathVariable Long userId) {
        logger.info("GET /api/cart/{}/total - Calculating cart total", userId);
        
        try {
            BigDecimal total = cartService.getCartTotal(userId);
            logger.info("Cart total calculated via API - User ID: {}, Total: {}", userId, total);
            return ResponseEntity.ok(ApiResponse.success("Cart total calculated", total));
        } catch (Exception e) {
            logger.error("Error calculating cart total via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable Long userId) {
        logger.info("DELETE /api/cart/{}/clear - Clearing cart", userId);
        
        try {
            cartService.clearCart(userId);
            logger.info("Cart cleared successfully via API - User ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
        } catch (Exception e) {
            logger.error("Error clearing cart via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
