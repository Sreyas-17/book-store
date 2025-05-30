package com.bookstore.bookstore_app.dto;

import jakarta.validation.constraints.NotNull;

public class VendorApprovalRequest {
    @NotNull
    private Long vendorId;

    @NotNull
    private Boolean approved;

    private String rejectionReason;

    // Constructors
    public VendorApprovalRequest() {}

    public VendorApprovalRequest(Long vendorId, Boolean approved, String rejectionReason) {
        this.vendorId = vendorId;
        this.approved = approved;
        this.rejectionReason = rejectionReason;
    }

    // Getters and Setters
    public Long getVendorId() { return vendorId; }
    public void setVendorId(Long vendorId) { this.vendorId = vendorId; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
