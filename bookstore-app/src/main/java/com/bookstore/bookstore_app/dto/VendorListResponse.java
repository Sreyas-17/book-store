package com.bookstore.bookstore_app.dto;

import com.bookstore.bookstore_app.entity.Vendor;
import java.time.LocalDateTime;

public class VendorListResponse {
    private Long vendorId;
    private String businessName;
    private String businessEmail;
    private String businessPhone;
    private boolean isApproved;
    private LocalDateTime createdAt;
    private UserListResponse user;

    // Constructors
    public VendorListResponse() {}

    public VendorListResponse(Vendor vendor) {
        this.vendorId = vendor.getId();
        this.businessName = vendor.getBusinessName();
        this.businessEmail = vendor.getBusinessEmail();
        this.businessPhone = vendor.getBusinessPhone();
        this.isApproved = vendor.isApproved();
        this.createdAt = vendor.getCreatedAt();
        this.user = new UserListResponse(vendor.getUser());
    }

    // Getters and Setters
    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessEmail() { return businessEmail; }
    public void setBusinessEmail(String businessEmail) { this.businessEmail = businessEmail; }

    public String getBusinessPhone() { return businessPhone; }
    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public UserListResponse getUser() { return user; }
    public void setUser(UserListResponse user) { this.user = user; }
}