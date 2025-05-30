package com.bookstore.bookstore_app.dto;

import jakarta.validation.constraints.NotNull;

public class BookApprovalRequest {
    @NotNull
    private Long bookId;

    @NotNull
    private Boolean approved;

    private String rejectionReason;

    // Constructors
    public BookApprovalRequest() {}

    public BookApprovalRequest(Long bookId, Boolean approved, String rejectionReason) {
        this.bookId = bookId;
        this.approved = approved;
        this.rejectionReason = rejectionReason;
    }

    // Getters and Setters
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}