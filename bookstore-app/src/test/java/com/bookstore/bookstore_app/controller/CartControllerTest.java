package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.CartItem;
import com.bookstore.bookstore_app.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Test
    public void testAddToCart() {
        Long userId = 1L;
        Long bookId = 1L;
        int quantity = 1;
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        when(cartService.addToCart(userId, bookId, quantity)).thenReturn(cartItem);

        ResponseEntity<ApiResponse<CartItem>> response = cartController.addToCart(userId, bookId, quantity);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    public void testRemoveFromCart() {
        Long userId = 1L;
        Long bookId = 1L;
        doNothing().when(cartService).removeFromCart(userId, bookId);

        ResponseEntity<ApiResponse<String>> response = cartController.removeFromCart(userId, bookId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Item removed from cart", response.getBody().getMessage());
    }

    @Test
    public void testUpdateQuantity() {
        Long userId = 1L;
        Long bookId = 1L;
        int quantity = 2;
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        when(cartService.updateQuantity(userId, bookId, quantity)).thenReturn(cartItem);

        ResponseEntity<ApiResponse<CartItem>> response = cartController.updateQuantity(userId, bookId, quantity);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    public void testGetCartItems() {
        Long userId = 1L;
        List<CartItem> cartItems = Collections.singletonList(new CartItem());
        when(cartService.getCartItems(userId)).thenReturn(cartItems);

        ResponseEntity<ApiResponse<List<CartItem>>> response = cartController.getCartItems(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testGetCartTotal() {
        Long userId = 1L;
        BigDecimal total = new BigDecimal("100.00");
        when(cartService.getCartTotal(userId)).thenReturn(total);

        ResponseEntity<ApiResponse<BigDecimal>> response = cartController.getCartTotal(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(total, response.getBody().getData());
    }

    @Test
    public void testClearCart() {
        Long userId = 1L;
        doNothing().when(cartService).clearCart(userId);

        ResponseEntity<ApiResponse<String>> response = cartController.clearCart(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Cart cleared successfully", response.getBody().getMessage());
    }
} 