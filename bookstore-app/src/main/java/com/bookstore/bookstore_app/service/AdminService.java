package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Review;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.ReviewRepository;
import com.bookstore.bookstore_app.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public Book approveBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setApproved(true);
        return bookRepository.save(book);
    }

    public Book disapproveBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setApproved(false);
        return bookRepository.save(book);
    }

    public Review approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setApproved(true);
        return reviewRepository.save(review);
    }

    public List<Book> getPendingBooks() {
        return bookRepository.findByIsApprovedFalse();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
}