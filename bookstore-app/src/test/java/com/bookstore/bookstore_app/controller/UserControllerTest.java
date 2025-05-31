package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.dto.LoginRequest;
import com.bookstore.bookstore_app.dto.LoginResponse;
import com.bookstore.bookstore_app.dto.RegisterRequest;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.service.UserService;
import com.bookstore.bookstore_app.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    @Test
    public void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        when(userService.register(request)).thenReturn("User registered successfully");

        ResponseEntity<ApiResponse<String>> response = userController.register(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    public void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        when(userService.login(request)).thenReturn("token");

        ResponseEntity<ApiResponse<LoginResponse>> response = userController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("token", response.getBody().getData());
    }

    @Test
    public void testGetUserProfile() {
        String token = "Bearer validToken";
        User user = new User();
        user.setEmail("test@example.com");
        when(jwtUtil.validateToken("validToken")).thenReturn(true);
        when(jwtUtil.getEmailFromToken("validToken")).thenReturn("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        ResponseEntity<ApiResponse<User>> response = userController.getUserProfile(token);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("test@example.com", response.getBody().getData().getEmail());
    }

    @Test
    public void testForgotPassword() {
        String email = "test@example.com";
        when(userService.resetPassword(email)).thenReturn("Password reset email sent");

        ResponseEntity<ApiResponse<String>> response = userController.forgotPassword(email);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Password reset email sent", response.getBody().getMessage());
    }

    @Test
    public void testVerifyUser() {
        Long userId = 1L;
        when(userService.verifyUser(userId)).thenReturn("User verified successfully");

        ResponseEntity<ApiResponse<String>> response = userController.verifyUser(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User verified successfully", response.getBody().getMessage());
    }

    @Test
    public void testUpdateProfile() {
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.updateUser(userId, user)).thenReturn(user);

        ResponseEntity<ApiResponse<User>> response = userController.updateProfile(userId, user);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("test@example.com", response.getBody().getData().getEmail());
    }

    @Test
    public void testLogout() {
        ResponseEntity<ApiResponse<String>> response = userController.logout();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Logged out successfully", response.getBody().getMessage());
    }
} 