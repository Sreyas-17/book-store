package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Order;
import com.bookstore.bookstore_app.service.OrderService;
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
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testCreateOrder() {
        Long userId = 1L;
        Long addressId = 1L;
        Order order = new Order();
        order.setId(1L);
        when(orderService.createOrder(userId, addressId)).thenReturn(order);

        ResponseEntity<ApiResponse<Order>> response = orderController.createOrder(userId, addressId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    public void testGetUserOrders() {
        Long userId = 1L;
        List<Order> orders = Collections.singletonList(new Order());
        when(orderService.getUserOrders(userId)).thenReturn(orders);

        ResponseEntity<ApiResponse<List<Order>>> response = orderController.getUserOrders(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testGetOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        when(orderService.getOrderById(orderId)).thenReturn(order);

        ResponseEntity<ApiResponse<Order>> response = orderController.getOrderById(orderId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(orderId, response.getBody().getData().getId());
    }

    @Test
    public void testUpdateOrderStatus() {
        Long orderId = 1L;
        Order.OrderStatus status = Order.OrderStatus.CONFIRMED;
        Order order = new Order();
        order.setId(orderId);
        when(orderService.updateOrderStatus(orderId, status)).thenReturn(order);

        ResponseEntity<ApiResponse<Order>> response = orderController.updateOrderStatus(orderId, status);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(orderId, response.getBody().getData().getId());
    }
} 