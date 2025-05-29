package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Wishlist;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.repository.WishlistRepository;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    private static final Logger logger = LogManager.getLogger(WishlistService.class);

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public Wishlist addToWishlist(Long userId, Long bookId) {
        logger.info("Adding book to wishlist - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("Add to wishlist failed - User not found with ID: {}", userId);
                        return new RuntimeException("User not found");
                    });

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.warn("Add to wishlist failed - Book not found with ID: {}", bookId);
                        return new RuntimeException("Book not found");
                    });

            Optional<Wishlist> existing = wishlistRepository.findByUserIdAndBookId(userId, bookId);
            if (existing.isPresent()) {
                logger.warn("Add to wishlist failed - Book already in wishlist for User ID: {}, Book ID: {}", userId, bookId);
                throw new RuntimeException("Book already in wishlist");
            }

            Wishlist wishlist = new Wishlist(user, book);
            Wishlist savedWishlist = wishlistRepository.save(wishlist);
            logger.info("Book added to wishlist successfully - Wishlist ID: {}, User ID: {}, Book: {}", 
                       savedWishlist.getId(), userId, book.getTitle());
            return savedWishlist;
            
        } catch (Exception e) {
            logger.error("Failed to add book to wishlist - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long bookId) {
        logger.info("Removing book from wishlist - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            wishlistRepository.deleteByUserIdAndBookId(userId, bookId);
            logger.info("Book removed from wishlist successfully - User ID: {}, Book ID: {}", userId, bookId);
        } catch (Exception e) {
            logger.error("Failed to remove book from wishlist - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            throw e;
        }
    }

    public List<Wishlist> getUserWishlist(Long userId) {
        logger.debug("Fetching wishlist for user ID: {}", userId);
        
        try {
            List<Wishlist> wishlist = wishlistRepository.findByUserId(userId);
            logger.info("Found {} items in wishlist for user ID: {}", wishlist.size(), userId);
            return wishlist;
        } catch (Exception e) {
            logger.error("Failed to fetch wishlist for user ID: {} - Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void moveToCart(Long userId, Long bookId) {
        logger.info("Moving book from wishlist to cart - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            cartService.addToCart(userId, bookId, 1);
            wishlistRepository.deleteByUserIdAndBookId(userId, bookId);
            logger.info("Book moved from wishlist to cart successfully - User ID: {}, Book ID: {}", userId, bookId);
        } catch (Exception e) {
            logger.error("Failed to move book from wishlist to cart - User ID: {}, Book ID: {} - Error: {}", 
                        userId, bookId, e.getMessage(), e);
            throw e;
        }
    }
}