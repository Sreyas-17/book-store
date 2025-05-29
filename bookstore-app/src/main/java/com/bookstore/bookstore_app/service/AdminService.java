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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

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

    public Vendor approveVendor(Long vendorId) {
        logger.info("Approving vendor with ID: {}", vendorId);
        try {
            Vendor vendor = vendorRepository.findById(vendorId)
                    .orElseThrow(() -> {
                        logger.warn("Vendor not found for approval with ID: {}", vendorId);
                        return new RuntimeException("Vendor not found");
                    });

            vendor.setApproved(true);
            Vendor savedVendor = vendorRepository.save(vendor);
            logger.info("Vendor approved successfully - ID: {}, Business: {}", 
                    savedVendor.getId(), savedVendor.getBusinessName());
            return savedVendor;
        } catch (Exception e) {
            logger.error("Failed to approve vendor with ID: {} - Error: {}", vendorId, e.getMessage(), e);
            throw e;
        }
    }

    public Vendor disapproveVendor(Long vendorId) {
        logger.info("Disapproving vendor with ID: {}", vendorId);
        try {
            Vendor vendor = vendorRepository.findById(vendorId)
                    .orElseThrow(() -> {
                        logger.warn("Vendor not found for disapproval with ID: {}", vendorId);
                        return new RuntimeException("Vendor not found");
                    });

            vendor.setApproved(false);
            Vendor savedVendor = vendorRepository.save(vendor);
            logger.info("Vendor disapproved successfully - ID: {}, Business: {}", 
                    savedVendor.getId(), savedVendor.getBusinessName());
            return savedVendor;
        } catch (Exception e) {
            logger.error("Failed to disapprove vendor with ID: {} - Error: {}", vendorId, e.getMessage(), e);
            throw e;
        }
    }

    public List<Vendor> getPendingVendors() {
        logger.debug("Fetching pending vendors for approval");
        try {
            List<Vendor> pendingVendors = vendorRepository.findByIsApprovedFalse();
            logger.info("Found {} pending vendors for approval", pendingVendors.size());
            return pendingVendors;
        } catch (Exception e) {
            logger.error("Failed to fetch pending vendors - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Map<String, Object> getAdminDashboard() {
        logger.debug("Generating admin dashboard data");
        try {
            // Get statistics
            long totalBooks = bookRepository.count();
            long approvedBooks = bookRepository.findByIsApprovedTrue().size();
            long pendingBooks = bookRepository.findByIsApprovedFalse().size();
            
            long totalVendors = vendorRepository.count();
            long approvedVendors = vendorRepository.findByIsApprovedTrue().size();
            long pendingVendors = vendorRepository.findByIsApprovedFalse().size();
            
            long totalReviews = reviewRepository.count();
            long approvedReviews = reviewRepository.findByIsApprovedTrue().size();
            long pendingReviews = reviewRepository.findByIsApprovedFalse().size();

            Map<String, Object> dashboard = Map.of(
                "books", Map.of(
                    "total", totalBooks,
                    "approved", approvedBooks,
                    "pending", pendingBooks
                ),
                "vendors", Map.of(
                    "total", totalVendors,
                    "approved", approvedVendors,
                    "pending", pendingVendors
                ),
                "reviews", Map.of(
                    "total", totalReviews,
                    "approved", approvedReviews,
                    "pending", pendingReviews
                ),
                "recentBooks", bookRepository.findByOrderByCreatedAtDesc(
                    PageRequest.of(0, 5)
                ).getContent(),
                "pendingApprovals", Map.of(
                    "books", Math.min(pendingBooks, 5),
                    "vendors", Math.min(pendingVendors, 5),
                    "reviews", Math.min(pendingReviews, 5)
                )
            );

            logger.info("Admin dashboard generated - {} books, {} vendors, {} reviews", 
                    totalBooks, totalVendors, totalReviews);
            return dashboard;
            
        } catch (Exception e) {
            logger.error("Failed to generate admin dashboard - Error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
