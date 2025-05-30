package com.bookstore.bookstore_app.repository;

import com.bookstore.bookstore_app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Order> findByOrderNumber(String orderNumber);

    List<Order> findAllByOrderByCreatedAtDesc();

    // REMOVED: long countAll();
    // Use the inherited count() method instead

    // Vendor queries - orders containing their books
    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.book.vendor.id = :vendorId ORDER BY o.createdAt DESC")
    List<Order> findOrdersByVendorId(@Param("vendorId") Long vendorId);

    @Query("SELECT COUNT(DISTINCT o) FROM Order o JOIN o.orderItems oi WHERE oi.book.vendor.id = :vendorId")
    long countOrdersByVendorId(@Param("vendorId") Long vendorId);

    // Revenue calculations
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal getTotalRevenue();

    @Query("SELECT COALESCE(SUM(oi.totalPrice), 0) FROM OrderItem oi WHERE oi.book.vendor.id = :vendorId")
    BigDecimal getVendorRevenue(@Param("vendorId") Long vendorId);
}