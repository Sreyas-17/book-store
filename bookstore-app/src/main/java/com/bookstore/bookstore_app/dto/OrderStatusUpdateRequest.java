package com.bookstore.bookstore_app.dto;

import com.bookstore.bookstore_app.entity.Order;
import jakarta.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {
    @NotNull
    private Long orderId;

    @NotNull
    private Order.OrderStatus newStatus;

    // Constructors
    public OrderStatusUpdateRequest() {}

    public OrderStatusUpdateRequest(Long orderId, Order.OrderStatus newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Order.OrderStatus getNewStatus() { return newStatus; }
    public void setNewStatus(Order.OrderStatus newStatus) { this.newStatus = newStatus; }
}