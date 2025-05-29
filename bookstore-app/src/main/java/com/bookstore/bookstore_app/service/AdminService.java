package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Review;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.ReviewRepository;
import com.bookstore.bookstore_app.repository.VendorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    private static final Logger logger = LogManager.getLogger(AdminService.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public Book approveBook(Long bookId) {
        logger.info("Approving book with ID: {}", bookId);
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.warn("Book not found for approval with ID: {}", bookId);
                        return new RuntimeException("Book not found");
                    });

            book.setApproved(true);
            Book savedBook = bookRepository.save(book);
            logger.info("Book approved successfully - ID: {}, Title: {}", savedBook.getId(), savedBook.getTitle());
            return savedBook;
        } catch (Exception e) {
            logger.error("Failed to approve book with ID: {} - Error: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }

    public Book disapproveBook(Long bookId) {
        logger.info("Disapproving book with ID: {}", bookId);
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.warn("Book not found for disapproval with ID: {}", bookId);
                        return new RuntimeException("Book not found");
                    });

            book.setApproved(false);
            Book savedBook = bookRepository.save(book);
            logger.info("Book disapproved successfully - ID: {}, Title: {}", savedBook.getId(), savedBook.getTitle());
            return savedBook;
        } catch (Exception e) {
            logger.error("Failed to disapprove book with ID: {} - Error: {}", bookId, e.getMessage(), e);
            throw e;
        }
    }

    public Review approveReview(Long reviewId) {
        logger.info("Approving review with ID: {}", reviewId);
        try {
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> {
                        logger.warn("Review not found for approval with ID: {}", reviewId);
                        return new RuntimeException("Review not found");
                    });

            review.setApproved(true);
            Review savedReview = reviewRepository.save(review);
            logger.info("Review approved successfully with ID: {}", savedReview.getId());
            return savedReview;
        } catch (Exception e) {
            logger.error("Failed to approve review with ID: {} - Error: {}", reviewId, e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> getPendingBooks() {
        logger.debug("Fetching pending books for approval");
        try {
            List<Book> pendingBooks = bookRepository.findByIsApprovedFalse();
            logger.info("Found {} pending books for approval", pendingBooks.size());
            return pendingBooks;
        } catch (Exception e) {
            logger.error("Failed to fetch pending books - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> getAllBooks() {
        logger.debug("Admin fetching all books");
        try {
            List<Book> books = bookRepository.findAll();
            logger.info("Admin retrieved {} books from database", books.size());
            return books;
        } catch (Exception e) {
            logger.error("Admin failed to fetch all books - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public List<Vendor> getAllVendors() {
        logger.debug("Admin fetching all vendors");
        try {
            List<Vendor> vendors = vendorRepository.findAll();
            logger.info("Admin retrieved {} vendors from database", vendors.size());
            return vendors;
        } catch (Exception e) {
            logger.error("Admin failed to fetch all vendors - Error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
