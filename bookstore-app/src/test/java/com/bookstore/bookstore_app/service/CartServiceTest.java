package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.CartItem;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.exception.BusinessException;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.CartItemRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Book book;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setPrice(new BigDecimal("19.99"));
        book.setStockQuantity(10);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUser(user);
        cartItem.setBook(book);
        cartItem.setQuantity(2);
    }

    @Test
    void addToCart_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByUserIdAndBookId(1L, 1L)).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.addToCart(1L, 1L, 2);

        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addToCart_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            cartService.addToCart(1L, 1L, 2);
        });

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void addToCart_InsufficientStock() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(BusinessException.class, () -> {
            cartService.addToCart(1L, 1L, 15);
        });

        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void removeFromCart_Success() {
        when(cartItemRepository.findByUserIdAndBookId(1L, 1L)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).deleteByUserIdAndBookId(1L, 1L);

        assertDoesNotThrow(() -> {
            cartService.removeFromCart(1L, 1L);
        });

        verify(cartItemRepository, times(1)).deleteByUserIdAndBookId(1L, 1L);
    }

    @Test
    void updateQuantity_Success() {
        when(cartItemRepository.findByUserIdAndBookId(1L, 1L)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartService.updateQuantity(1L, 1L, 3);

        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void updateQuantity_ZeroQuantity() {
        when(cartItemRepository.findByUserIdAndBookId(1L, 1L)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).deleteByUserIdAndBookId(1L, 1L);

        CartItem result = cartService.updateQuantity(1L, 1L, 0);

        assertNull(result);
        verify(cartItemRepository, times(1)).deleteByUserIdAndBookId(1L, 1L);
    }

    @Test
    void getCartItems_Success() {
        List<CartItem> expectedItems = Arrays.asList(cartItem);
        when(cartItemRepository.findByUserId(1L)).thenReturn(expectedItems);

        List<CartItem> result = cartService.getCartItems(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(cartItem.getId(), result.get(0).getId());
        verify(cartItemRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getCartTotal_Success() {
        List<CartItem> cartItems = Arrays.asList(cartItem);
        when(cartItemRepository.findByUserId(1L)).thenReturn(cartItems);

        BigDecimal total = cartService.getCartTotal(1L);

        assertNotNull(total);
        assertEquals(new BigDecimal("39.98"), total);
        verify(cartItemRepository, times(1)).findByUserId(1L);
    }

    @Test
    void clearCart_Success() {
        doNothing().when(cartItemRepository).deleteByUserId(1L);

        assertDoesNotThrow(() -> {
            cartService.clearCart(1L);
        });

        verify(cartItemRepository, times(1)).deleteByUserId(1L);
    }
} 