package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.*;
import com.bookstore.bookstore_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address shippingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        List<CartItem> cartItems = cartService.getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalAmount = cartService.getCartTotal(userId);
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Order order = new Order(user, orderNumber, totalAmount);
        order.setShippingAddress(shippingAddress);

// Save the order to generate an ID
        orderRepository.save(order);

// Now create order items using the same `order`
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        order,
                        cartItem.getBook(),
                        cartItem.getQuantity(),
                        cartItem.getBook().getPrice()
                ))
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

// Clear the cart
        cartService.clearCart(userId);

// Save again after setting order items
        return orderRepository.save(order);

    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}