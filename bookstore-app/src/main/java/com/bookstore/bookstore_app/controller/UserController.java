// Update your UserController.java with this version
package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.*;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.service.UserService;
import com.bookstore.bookstore_app.config.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String message = userService.register(request);
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = userService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Check if Authorization header is present
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(ApiResponse.error("Authorization header missing or invalid"));
            }

            // Extract token
            String token = authHeader.replace("Bearer ", "");

            // Validate token
            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(401).body(ApiResponse.error("Invalid or expired token"));
            }

            // Get email from token
            String email = jwtUtil.getEmailFromToken(token);
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(401).body(ApiResponse.error("Invalid token payload"));
            }

            // Get user
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
            }

            return ResponseEntity.ok(ApiResponse.success("Profile retrieved", user));

        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(500).body(ApiResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        try {
            String message = userService.resetPassword(email);
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/verify/{userId}")
    public ResponseEntity<ApiResponse<String>> verifyUser(@PathVariable Long userId) {
        try {
            String message = userService.verifyUser(userId);
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<User>> updateProfile(@PathVariable Long userId, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(userId, user);
            return ResponseEntity.ok(ApiResponse.success("Profile updated", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }
}