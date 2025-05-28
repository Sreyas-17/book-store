// Fixed CartController.java - Add proper error handling and logging
package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.CartItem;
import com.bookstore.bookstore_app.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") int quantity) {
        try {
            System.out.println("🛒 CartController: Adding to cart - userId: " + userId + ", bookId: " + bookId + ", quantity: " + quantity);
            CartItem cartItem = cartService.addToCart(userId, bookId, quantity);
            System.out.println("✅ CartController: Item added successfully - cartItemId: " + cartItem.getId());
            return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartItem));
        } catch (Exception e) {
            System.err.println("❌ CartController: Error adding to cart - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<String>> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        try {
            System.out.println("🗑️ CartController: Removing from cart - userId: " + userId + ", bookId: " + bookId);
            cartService.removeFromCart(userId, bookId);
            System.out.println("✅ CartController: Item removed successfully");
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart"));
        } catch (Exception e) {
            System.err.println("❌ CartController: Error removing from cart - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/update-quantity")
    public ResponseEntity<ApiResponse<CartItem>> updateQuantity(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam int quantity) {
        try {
            System.out.println("🔄 CartController: Updating quantity - userId: " + userId + ", bookId: " + bookId + ", quantity: " + quantity);
            CartItem cartItem = cartService.updateQuantity(userId, bookId, quantity);
            System.out.println("✅ CartController: Quantity updated successfully");
            return ResponseEntity.ok(ApiResponse.success("Quantity updated", cartItem));
        } catch (Exception e) {
            System.err.println("❌ CartController: Error updating quantity - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(@PathVariable Long userId) {
        try {
            System.out.println("📋 CartController: Getting cart items for userId: " + userId);
            List<CartItem> cartItems = cartService.getCartItems(userId);
            System.out.println("✅ CartController: Found " + cartItems.size() + " cart items");

            // Log each cart item for debugging
            for (CartItem item : cartItems) {
                System.out.println("  - CartItem ID: " + item.getId() +
                        ", Book: " + (item.getBook() != null ? item.getBook().getTitle() : "null") +
                        ", Quantity: " + item.getQuantity());
            }

            return ResponseEntity.ok(ApiResponse.success("Cart items retrieved", cartItems));
        } catch (Exception e) {
            System.err.println("❌ CartController: Error getting cart items - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to retrieve cart items: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<ApiResponse<BigDecimal>> getCartTotal(@PathVariable Long userId) {
        try {
            System.out.println("💰 CartController: Getting cart total for userId: " + userId);
            BigDecimal total = cartService.getCartTotal(userId);
            System.out.println("✅ CartController: Cart total calculated: " + total);
            return ResponseEntity.ok(ApiResponse.success("Cart total calculated", total));
        } catch (Exception e) {
            System.err.println("❌ CartController: Error calculating cart total - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable Long userId) {
        try {
            System.out.println("🧹 CartController: Clearing cart for userId: " + userId);
            cartService.clearCart(userId);
            System.out.println("✅ CartController: Cart cleared successfully");
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
        } catch (Exception e) {
            System.err.println("❌ CartController: Error clearing cart - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}