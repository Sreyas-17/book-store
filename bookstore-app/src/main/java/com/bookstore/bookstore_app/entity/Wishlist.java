package com.bookstore.bookstore_app.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlists")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Add this to fix serialization
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)  // Changed from LAZY to EAGER
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore  // Don't include user in wishlist to prevent recursion
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)  // Changed from LAZY to EAGER
    @JoinColumn(name = "book_id", nullable = false)
    // Keep book visible in wishlist (users need to see book details)
    private Book book;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();

    // Constructors
    public Wishlist() {}

    public Wishlist(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}