package com.bookstore.bookstore_app.dto;

import java.math.BigDecimal;

public class VendorDashboardDTO {
    private long totalBooks;
    private long approvedBooks;
    private long pendingBooks;
    private long rejectedBooks;
    private long totalOrders;
    private BigDecimal totalRevenue;

    // Constructors
    public VendorDashboardDTO() {}

    public VendorDashboardDTO(long totalBooks, long approvedBooks, long pendingBooks, 
                            long rejectedBooks, long totalOrders, BigDecimal totalRevenue) {
        this.totalBooks = totalBooks;
        this.approvedBooks = approvedBooks;
        this.pendingBooks = pendingBooks;
        this.rejectedBooks = rejectedBooks;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }

    public long getApprovedBooks() { return approvedBooks; }
    public void setApprovedBooks(long approvedBooks) { this.approvedBooks = approvedBooks; }

    public long getPendingBooks() { return pendingBooks; }
    public void setPendingBooks(long pendingBooks) { this.pendingBooks = pendingBooks; }

    public long getRejectedBooks() { return rejectedBooks; }
    public void setRejectedBooks(long rejectedBooks) { this.rejectedBooks = rejectedBooks; }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}