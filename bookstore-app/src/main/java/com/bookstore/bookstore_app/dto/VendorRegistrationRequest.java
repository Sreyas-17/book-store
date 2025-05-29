package com.bookstore.bookstore_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VendorRegistrationRequest {

    @NotBlank(message = "Business name is required")
    @Size(min = 2, max = 100, message = "Business name must be between 2 and 100 characters")
    private String businessName;

    @Email(message = "Valid business email is required")
    @NotBlank(message = "Business email is required")
    private String businessEmail;

    @NotBlank(message = "Business phone is required")
    @Size(min = 10, max = 15, message = "Business phone must be between 10 and 15 characters")
    private String businessPhone;

    // Constructors
    public VendorRegistrationRequest() {}

    public VendorRegistrationRequest(String businessName, String businessEmail, String businessPhone) {
        this.businessName = businessName;
        this.businessEmail = businessEmail;
        this.businessPhone = businessPhone;
    }

    // Getters and Setters
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessEmail() { return businessEmail; }
    public void setBusinessEmail(String businessEmail) { this.businessEmail = businessEmail; }

    public String getBusinessPhone() { return businessPhone; }
    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }

    @Override
    public String toString() {
        return "VendorRegistrationRequest{" +
                "businessName='" + businessName + '\'' +
                ", businessEmail='" + businessEmail + '\'' +
                ", businessPhone='" + businessPhone + '\'' +
                '}';
    }
}