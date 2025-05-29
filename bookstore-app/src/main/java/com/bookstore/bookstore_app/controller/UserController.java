package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.*;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.service.UserService;
import com.bookstore.bookstore_app.config.JwtUtil;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("POST /api/auth/register - Registration attempt for email: {}", request.getEmail());
        
        try {
            String message = userService.register(request);
            logger.info("User registration successful via API - Email: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            logger.error("Error during registration via API - Email: {} - Error: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("POST /api/auth/login - Login attempt for email: {}", request.getEmail());
    
        try {
        LoginResponse loginResponse = userService.loginWithRoleInfo(request);
        
        logger.info("User login successful via API - Email: {}, Role: {}", 
                   request.getEmail(), loginResponse.getRole());
        
        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
        
        } catch (Exception e) {
            logger.error("Error during login via API - Email: {} - Error: {}", 
                    request.getEmail(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
}

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<User>> getUserProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("GET /api/auth/profile - Fetching user profile");
        
        try {
            // Check if Authorization header is present
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.warn("Profile request failed - Authorization header missing or invalid");
                return ResponseEntity.status(401).body(ApiResponse.error("Authorization header missing or invalid"));
            }

            // Extract token
            String token = authHeader.replace("Bearer ", "");

            // Validate token
            if (!jwtUtil.validateToken(token)) {
                logger.warn("Profile request failed - Invalid or expired token");
                return ResponseEntity.status(401).body(ApiResponse.error("Invalid or expired token"));
            }

            // Get email from token
            String email = jwtUtil.getEmailFromToken(token);
            if (email == null || email.isEmpty()) {
                logger.warn("Profile request failed - Invalid token payload");
                return ResponseEntity.status(401).body(ApiResponse.error("Invalid token payload"));
            }

            // Get user
            User user = userService.getUserByEmail(email);
            if (user == null) {
                logger.warn("Profile request failed - User not found for email: {}", email);
                return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
            }

            logger.info("User profile retrieved successfully via API - Email: {}", email);
            return ResponseEntity.ok(ApiResponse.success("Profile retrieved", user));

        } catch (Exception e) {
            logger.error("Error retrieving user profile via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(ApiResponse.error("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        logger.info("POST /api/auth/forgot-password - Password reset request for email: {}", email);
        
        try {
            String message = userService.resetPassword(email);
            logger.info("Password reset processed via API - Email: {}", email);
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            logger.error("Error processing password reset via API - Email: {} - Error: {}", email, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/verify/{userId}")
    public ResponseEntity<ApiResponse<String>> verifyUser(@PathVariable Long userId) {
        logger.info("POST /api/auth/verify/{} - User verification request", userId);
        
        try {
            String message = userService.verifyUser(userId);
            logger.info("User verification successful via API - User ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            logger.error("Error during user verification via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<User>> updateProfile(@PathVariable Long userId, @RequestBody User user) {
        logger.info("PUT /api/auth/profile/{} - Updating user profile", userId);
        
        try {
            User updatedUser = userService.updateUser(userId, user);
            logger.info("User profile updated successfully via API - User ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success("Profile updated", updatedUser));
        } catch (Exception e) {
            logger.error("Error updating user profile via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        logger.info("POST /api/auth/logout - User logout");
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
    }

    @PostMapping("/register-vendor")
    public ResponseEntity<ApiResponse<String>> registerAsVendor(@Valid @RequestBody RegisterRequest request) {
        logger.info("POST /api/auth/register-vendor - Vendor registration attempt for email: {}", request.getEmail());
    
        try {
            String message = userService.registerWithRole(request, User.Role.VENDOR);
            logger.info("Vendor user registration successful via API - Email: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            logger.error("Error during vendor registration via API - Email: {} - Error: {}", 
                    request.getEmail(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<String>> registerAsAdmin(@Valid @RequestBody RegisterRequest request) {
        logger.info("POST /api/auth/register-admin - Admin registration attempt for email: {}", request.getEmail());

        try {
            String message = userService.registerWithRole(request, User.Role.ADMIN);
            logger.info("Admin user registration successful via API - Email: {}", request.getEmail());
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            logger.error("Error during admin registration via API - Email: {} - Error: {}",
                    request.getEmail(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}