package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.dto.VendorRegistrationRequest;
import com.bookstore.bookstore_app.dto.BookCreateRequest;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Order;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.OrderRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import com.bookstore.bookstore_app.repository.VendorRepository;
import com.bookstore.bookstore_app.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorService {

    private static final Logger logger = LogManager.getLogger(VendorService.class);

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Vendor registerVendor(Long userId, VendorRegistrationRequest request) {
        logger.info("Registering vendor for user ID: {}", userId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("User not found for vendor registration: {}", userId);
                        return new BusinessException("User not found");
                    });

            // Check if user is already a vendor
            Optional<Vendor> existingVendor = vendorRepository.findByUserId(userId);
            if (existingVendor.isPresent()) {
                logger.warn("User {} already has a vendor account", userId);
                throw new BusinessException("User already has a vendor account");
            }

            // Update user role to VENDOR
            user.setRole(User.Role.VENDOR);
            userRepository.save(user);

            // Create vendor profile
            Vendor vendor = new Vendor();
            vendor.setUser(user);
            vendor.setBusinessName(request.getBusinessName());
            vendor.setBusinessEmail(request.getBusinessEmail());
            vendor.setBusinessPhone(request.getBusinessPhone());
            vendor.setApproved(false); // Requires admin approval

            Vendor savedVendor = vendorRepository.save(vendor);
            logger.info("Vendor registration completed - ID: {}, Business: {}", 
                       savedVendor.getId(), savedVendor.getBusinessName());

            return savedVendor;
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error registering vendor for user ID: {}", userId, ex);
            throw new RuntimeException("Vendor registration failed");
        }
    }

    public Vendor getVendorProfile(Long userId) {
        logger.debug("Fetching vendor profile for user ID: {}", userId);
        
        try {
            return vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.warn("Vendor profile not found for user ID: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });
        } catch (Exception ex) {
            logger.error("Error fetching vendor profile for user ID: {}", userId, ex);
            throw new RuntimeException("Failed to fetch vendor profile");
        }
    }

    public Vendor updateVendorProfile(Long userId, VendorRegistrationRequest request) {
        logger.info("Updating vendor profile for user ID: {}", userId);
        
        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.error("Vendor not found for profile update: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            vendor.setBusinessName(request.getBusinessName());
            vendor.setBusinessEmail(request.getBusinessEmail());
            vendor.setBusinessPhone(request.getBusinessPhone());

            Vendor updatedVendor = vendorRepository.save(vendor);
            logger.info("Vendor profile updated successfully - ID: {}", updatedVendor.getId());

            return updatedVendor;
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error updating vendor profile for user ID: {}", userId, ex);
            throw new RuntimeException("Failed to update vendor profile");
        }
    }

    @Transactional
    public Book addBook(Long userId, BookCreateRequest request) {
        logger.info("Vendor adding book - User ID: {}, Title: {}", userId, request.getTitle());
        
        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.error("Vendor not found for book creation: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            if (!vendor.isApproved()) {
                logger.warn("Unapproved vendor attempting to add book: {}", userId);
                throw new BusinessException("Vendor account not approved yet");
            }

            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setIsbn(request.getIsbn());
            book.setDescription(request.getDescription());
            book.setPrice(request.getPrice());
            book.setStockQuantity(request.getStockQuantity());
            book.setImageUrl(request.getImageUrl());
            book.setVendor(vendor);
            book.setApproved(false); // Requires admin approval

            Book savedBook = bookRepository.save(book);
            logger.info("Book added successfully - ID: {}, Title: {}, Vendor: {}", 
                       savedBook.getId(), savedBook.getTitle(), vendor.getBusinessName());

            return savedBook;
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error adding book for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to add book");
        }
    }

    public List<Book> getVendorBooks(Long userId) {
        logger.debug("Fetching books for vendor user ID: {}", userId);
        
        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.warn("Vendor not found for books fetch: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            List<Book> books = vendor.getBooks();
            logger.info("Retrieved {} books for vendor: {}", books.size(), vendor.getBusinessName());
            
            return books;
            
        } catch (Exception ex) {
            logger.error("Error fetching books for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to fetch vendor books");
        }
    }

    public Book updateBook(Long userId, Long bookId, BookCreateRequest request) {
        logger.info("Vendor updating book - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.error("Vendor not found for book update: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.error("Book not found for update: {}", bookId);
                        return new RuntimeException("Book not found");
                    });

            // Ensure the book belongs to this vendor
            if (!book.getVendor().getId().equals(vendor.getId())) {
                logger.warn("Vendor {} attempting to update book {} owned by different vendor", 
                           userId, bookId);
                throw new BusinessException("You can only update your own books");
            }

            book.setTitle(request.getTitle());
            book.setAuthor(request.getAuthor());
            book.setIsbn(request.getIsbn());
            book.setDescription(request.getDescription());
            book.setPrice(request.getPrice());
            book.setStockQuantity(request.getStockQuantity());
            book.setImageUrl(request.getImageUrl());
            book.setApproved(false); // Requires re-approval after changes

            Book updatedBook = bookRepository.save(book);
            logger.info("Book updated successfully - ID: {}, Title: {}", 
                       updatedBook.getId(), updatedBook.getTitle());

            return updatedBook;
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error updating book for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to update book");
        }
    }

    @Transactional
    public void deleteBook(Long userId, Long bookId) {
        logger.info("Vendor deleting book - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.error("Vendor not found for book deletion: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.error("Book not found for deletion: {}", bookId);
                        return new RuntimeException("Book not found");
                    });

            // Ensure the book belongs to this vendor
            if (!book.getVendor().getId().equals(vendor.getId())) {
                logger.warn("Vendor {} attempting to delete book {} owned by different vendor", 
                           userId, bookId);
                throw new BusinessException("You can only delete your own books");
            }

            bookRepository.delete(book);
            logger.info("Book deleted successfully - ID: {}, Title: {}", bookId, book.getTitle());
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error deleting book for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to delete book");
        }
    }

    public Map<String, Object> getVendorDashboard(Long userId) {
        logger.debug("Generating dashboard data for vendor user ID: {}", userId);
        
        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.warn("Vendor not found for dashboard: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            List<Book> vendorBooks = vendor.getBooks();
            
            // Calculate statistics
            int totalBooks = vendorBooks.size();
            int approvedBooks = (int) vendorBooks.stream().filter(Book::isApproved).count();
            int pendingBooks = totalBooks - approvedBooks;
            
            BigDecimal totalRevenue = calculateTotalRevenue(vendor.getId());
            int totalOrders = getTotalOrdersCount(vendor.getId());

            Map<String, Object> dashboard = Map.of(
                "vendorInfo", vendor,
                "totalBooks", totalBooks,
                "approvedBooks", approvedBooks,
                "pendingBooks", pendingBooks,
                "totalRevenue", totalRevenue,
                "totalOrders", totalOrders,
                "recentBooks", vendorBooks.stream().limit(5).collect(Collectors.toList())
            );

            logger.info("Dashboard generated for vendor: {} - {} books, {} orders", 
                       vendor.getBusinessName(), totalBooks, totalOrders);

            return dashboard;
            
        } catch (Exception ex) {
            logger.error("Error generating dashboard for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to generate vendor dashboard");
        }
    }

    private BigDecimal calculateTotalRevenue(Long vendorId) {
        try {
            return orderRepository.getVendorRevenue(vendorId);
        } catch (Exception e) {
            logger.error("Error calculating revenue for vendor ID: {}", vendorId, e);
            return BigDecimal.ZERO;
        }
    }

    private int getTotalOrdersCount(Long vendorId) {
        try {
            return (int) orderRepository.countOrdersByVendorId(vendorId);
        } catch (Exception e) {
            logger.error("Error counting orders for vendor ID: {}", vendorId, e);
            return 0;
        }
    }

    public List<Order> getVendorOrders(Long userId) {
        logger.debug("Fetching orders for vendor user ID: {}", userId);

        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.warn("Vendor not found for orders fetch: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            List<Order> orders = orderRepository.findOrdersByVendorId(vendor.getId());
            logger.info("Retrieved {} orders for vendor: {}", orders.size(), vendor.getBusinessName());

            return orders;

        } catch (Exception ex) {
            logger.error("Error fetching orders for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to fetch vendor orders");
        }
    }

    // Add method to update order status:
    public Order updateOrderStatus(Long userId, Long orderId, Order.OrderStatus newStatus) {
        logger.info("Vendor updating order status - User ID: {}, Order ID: {}, Status: {}", userId, orderId, newStatus);

        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> new BusinessException("Vendor profile not found"));

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Verify vendor has items in this order
            boolean hasVendorItems = order.getOrderItems().stream()
                    .anyMatch(item -> item.getBook().getVendor().getId().equals(vendor.getId()));

            if (!hasVendorItems) {
                throw new BusinessException("You can only update orders containing your books");
            }

            order.setStatus(newStatus);
            Order updatedOrder = orderRepository.save(order);

            logger.info("Order status updated by vendor - Order ID: {}, New Status: {}", orderId, newStatus);
            return updatedOrder;

        } catch (Exception ex) {
            logger.error("Error updating order status for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to update order status");
        }
    }

    public Book updateBookImage(Long userId, Long bookId, String imageUrl) {
        logger.info("Vendor updating book image - User ID: {}, Book ID: {}, Image: {}", userId, bookId, imageUrl);

        try {
            Vendor vendor = vendorRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        logger.error("Vendor not found for image update: {}", userId);
                        return new BusinessException("Vendor profile not found");
                    });

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.error("Book not found for image update: {}", bookId);
                        return new RuntimeException("Book not found");
                    });

            // Ensure the book belongs to this vendor
            if (!book.getVendor().getId().equals(vendor.getId())) {
                logger.warn("Vendor {} attempting to update image for book {} owned by different vendor",
                        userId, bookId);
                throw new BusinessException("You can only update images for your own books");
            }

            book.setImageUrl(imageUrl);
            Book updatedBook = bookRepository.save(book);

            logger.info("Book image updated successfully - Book ID: {}, New Image: {}",
                    bookId, imageUrl);
            return updatedBook;

        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Error updating book image for vendor user ID: {}", userId, ex);
            throw new RuntimeException("Failed to update book image");
        }
    }
}