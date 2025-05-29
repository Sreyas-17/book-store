package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.*;
import com.bookstore.bookstore_app.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LogManager.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Order createOrder(Long userId, Long addressId) {
        logger.info("Creating order for user ID: {} with address ID: {}", userId, addressId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("Order creation failed - User not found with ID: {}", userId);
                        return new RuntimeException("User not found");
                    });

            Address shippingAddress = addressRepository.findById(addressId)
                    .orElseThrow(() -> {
                        logger.warn("Order creation failed - Address not found with ID: {}", addressId);
                        return new RuntimeException("Address not found");
                    });

            List<CartItem> cartItems = cartService.getCartItems(userId);
            if (cartItems.isEmpty()) {
                logger.warn("Order creation failed - Cart is empty for user ID: {}", userId);
                throw new RuntimeException("Cart is empty");
            }

            BigDecimal totalAmount = cartService.getCartTotal(userId);
            String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            logger.debug("Order details - Number: {}, Total: {}, Items: {}", orderNumber, totalAmount, cartItems.size());

            Order order = new Order(user, orderNumber, totalAmount);
            order.setShippingAddress(shippingAddress);

            orderRepository.save(order);

            List<OrderItem> orderItems = cartItems.stream()
                    .map(cartItem -> new OrderItem(
                            order,
                            cartItem.getBook(),
                            cartItem.getQuantity(),
                            cartItem.getBook().getPrice()
                    ))
                    .collect(Collectors.toList());

            order.setOrderItems(orderItems);

            cartService.clearCart(userId);

            Order savedOrder = orderRepository.save(order);
            logger.info("Order created successfully - Order ID: {}, Number: {}, Total: {}", 
                       savedOrder.getId(), savedOrder.getOrderNumber(), savedOrder.getTotalAmount());
            
            return savedOrder;

        } catch (Exception e) {
            logger.error("Failed to create order for user ID: {} - Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    public List<Order> getUserOrders(Long userId) {
        logger.debug("Fetching orders for user ID: {}", userId);
        try {
            List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
            logger.info("Found {} orders for user ID: {}", orders.size(), userId);
            return orders;
        } catch (Exception e) {
            logger.error("Failed to fetch orders for user ID: {} - Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    public Order getOrderById(Long orderId) {
        logger.debug("Fetching order by ID: {}", orderId);
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> {
                        logger.warn("Order not found with ID: {}", orderId);
                        return new RuntimeException("Order not found");
                    });
            logger.debug("Found order - ID: {}, Number: {}", order.getId(), order.getOrderNumber());
            return order;
        } catch (Exception e) {
            logger.error("Failed to fetch order with ID: {} - Error: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        logger.info("Updating order status - Order ID: {}, New Status: {}", orderId, status);
        try {
            Order order = getOrderById(orderId);
            Order.OrderStatus oldStatus = order.getStatus();
            order.setStatus(status);
            Order savedOrder = orderRepository.save(order);
            logger.info("Order status updated successfully - Order ID: {}, Old Status: {}, New Status: {}", 
                       orderId, oldStatus, status);
            return savedOrder;
        } catch (Exception e) {
            logger.error("Failed to update order status - Order ID: {}, Status: {} - Error: {}", 
                        orderId, status, e.getMessage(), e);
            throw e;
        }
    }
}
