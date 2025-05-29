package com.bookstore.bookstore_app.dto;

import com.bookstore.bookstore_app.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token;
    private String role;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isVerified;
    
    // Vendor-specific info (only included if role is VENDOR)
    private Long vendorId;
    private String businessName;
    private boolean vendorApproved;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, User user) {
        this.token = token;
        this.role = user.getRole().toString();
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.isVerified = user.isVerified();
    }

    public LoginResponse(String token, User user, Long vendorId, String businessName, boolean vendorApproved) {
        this(token, user);
        this.vendorId = vendorId;
        this.businessName = businessName;
        this.vendorApproved = vendorApproved;
    }

    // Static factory methods
    public static LoginResponse forUser(String token, User user) {
        return new LoginResponse(token, user);
    }

    public static LoginResponse forVendor(String token, User user, Long vendorId, String businessName, boolean vendorApproved) {
        return new LoginResponse(token, user, vendorId, businessName, vendorApproved);
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public boolean isVendorApproved() { return vendorApproved; }
    public void setVendorApproved(boolean vendorApproved) { this.vendorApproved = vendorApproved; }
}