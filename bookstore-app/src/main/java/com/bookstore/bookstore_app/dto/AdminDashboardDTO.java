package com.bookstore.bookstore_app.dto;

import java.math.BigDecimal;

public class AdminDashboardDTO {
    private long totalUsers;
    private long totalVendors;
    private long pendingVendorApprovals;
    private long totalBooks;
    private long pendingBookApprovals;
    private long totalOrders;
    private BigDecimal totalRevenue;

    // Constructors
    public AdminDashboardDTO() {}

    public AdminDashboardDTO(long totalUsers, long totalVendors, long pendingVendorApprovals, 
                           long totalBooks, long pendingBookApprovals, long totalOrders, BigDecimal totalRevenue) {
        this.totalUsers = totalUsers;
        this.totalVendors = totalVendors;
        this.pendingVendorApprovals = pendingVendorApprovals;
        this.totalBooks = totalBooks;
        this.pendingBookApprovals = pendingBookApprovals;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalVendors() { return totalVendors; }
    public void setTotalVendors(long totalVendors) { this.totalVendors = totalVendors; }

    public long getPendingVendorApprovals() { return pendingVendorApprovals; }
    public void setPendingVendorApprovals(long pendingVendorApprovals) { this.pendingVendorApprovals = pendingVendorApprovals; }

    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long totalBooks) { this.totalBooks = totalBooks; }

    public long getPendingBookApprovals() { return pendingBookApprovals; }
    public void setPendingBookApprovals(long pendingBookApprovals) { this.pendingBookApprovals = pendingBookApprovals; }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}