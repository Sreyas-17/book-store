package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Wishlist;
import com.bookstore.bookstore_app.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WishlistControllerTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @Test
    public void testAddToWishlist() {
        Long userId = 1L;
        Long bookId = 1L;
        Wishlist wishlist = new Wishlist();
        wishlist.setId(1L);
        when(wishlistService.addToWishlist(userId, bookId)).thenReturn(wishlist);

        ResponseEntity<ApiResponse<Wishlist>> response = wishlistController.addToWishlist(userId, bookId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    public void testRemoveFromWishlist() {
        Long userId = 1L;
        Long bookId = 1L;
        doNothing().when(wishlistService).removeFromWishlist(userId, bookId);

        ResponseEntity<ApiResponse<String>> response = wishlistController.removeFromWishlist(userId, bookId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Book removed from wishlist", response.getBody().getMessage());
    }

    @Test
    public void testGetUserWishlist() {
        Long userId = 1L;
        List<Wishlist> wishlist = Collections.singletonList(new Wishlist());
        when(wishlistService.getUserWishlist(userId)).thenReturn(wishlist);

        ResponseEntity<ApiResponse<List<Wishlist>>> response = wishlistController.getUserWishlist(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testMoveToCart() {
        Long userId = 1L;
        Long bookId = 1L;
        doNothing().when(wishlistService).moveToCart(userId, bookId);

        ResponseEntity<ApiResponse<String>> response = wishlistController.moveToCart(userId, bookId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Book moved to cart", response.getBody().getMessage());
    }
} 