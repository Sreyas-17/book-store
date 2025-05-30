package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.*;
import com.bookstore.bookstore_app.repository.*;
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

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    private User user;
    private Address address;
    private CartItem cartItem;
    private Book book;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        address = new Address();
        address.setId(1L);
        address.setUser(user);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setPrice(new BigDecimal("19.99"));

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUser(user);
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderNumber("ORD-12345678");
        order.setTotalAmount(new BigDecimal("39.98"));
        order.setStatus(Order.OrderStatus.PENDING);
    }

    @Test
    void createOrder_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(cartService.getCartItems(1L)).thenReturn(Arrays.asList(cartItem));
        when(cartService.getCartTotal(1L)).thenReturn(new BigDecimal("39.98"));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.createOrder(1L, 1L);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderNumber(), result.getOrderNumber());
        verify(cartService, times(1)).clearCart(1L);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void createOrder_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L, 1L);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_AddressNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L, 1L);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrder_EmptyCart() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(cartService.getCartItems(1L)).thenReturn(Arrays.asList());

        assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(1L, 1L);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getUserOrders_Success() {
        List<Order> expectedOrders = Arrays.asList(order);
        when(orderRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(expectedOrders);

        List<Order> result = orderService.getUserOrders(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order.getId(), result.get(0).getId());
        verify(orderRepository, times(1)).findByUserIdOrderByCreatedAtDesc(1L);
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(order.getId(), result.getId());
        assertEquals(order.getOrderNumber(), result.getOrderNumber());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            orderService.getOrderById(1L);
        });

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void updateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrderStatus(1L, Order.OrderStatus.SHIPPED);

        assertNotNull(result);
        assertEquals(Order.OrderStatus.SHIPPED, result.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
} 