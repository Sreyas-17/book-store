package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Order;
import com.bookstore.bookstore_app.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @RequestParam Long userId,
            @RequestParam Long addressId) {
        logger.info("POST /api/orders/create - Creating order for user ID: {}, address ID: {}", userId, addressId);
        
        try {
            Order order = orderService.createOrder(userId, addressId);
            logger.info("Order created successfully via API - Order ID: {}, Number: {}, Total: {}", 
                       order.getId(), order.getOrderNumber(), order.getTotalAmount());
            return ResponseEntity.ok(ApiResponse.success("Order created successfully", order));
        } catch (Exception e) {
            logger.error("Error creating order via API - User ID: {}, Address ID: {} - Error: {}", 
                        userId, addressId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(@PathVariable Long userId) {
        logger.info("GET /api/orders/user/{} - Fetching user orders", userId);
        
        try {
            List<Order> orders = orderService.getUserOrders(userId);
            logger.info("Retrieved {} orders via API for user ID: {}", orders.size(), userId);
            return ResponseEntity.ok(ApiResponse.success("Orders retrieved", orders));
        } catch (Exception e) {
            logger.error("Error fetching user orders via API - User ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long orderId) {
        logger.info("GET /api/orders/{} - Fetching order by ID", orderId);
        
        try {
            Order order = orderService.getOrderById(orderId);
            logger.info("Order retrieved via API - Order ID: {}, Number: {}", order.getId(), order.getOrderNumber());
            return ResponseEntity.ok(ApiResponse.success("Order retrieved", order));
        } catch (Exception e) {
            logger.error("Error fetching order via API - Order ID: {} - Error: {}", orderId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        logger.info("PUT /api/orders/{}/status - Updating order status to: {}", orderId, status);
        
        try {
            Order order = orderService.updateOrderStatus(orderId, status);
            logger.info("Order status updated successfully via API - Order ID: {}, New Status: {}", orderId, status);
            return ResponseEntity.ok(ApiResponse.success("Order status updated", order));
        } catch (Exception e) {
            logger.error("Error updating order status via API - Order ID: {}, Status: {} - Error: {}", 
                        orderId, status, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}