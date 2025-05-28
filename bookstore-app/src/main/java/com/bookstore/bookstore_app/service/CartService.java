package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.CartItem;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.repository.CartItemRepository;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import com.bookstore.bookstore_app.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public CartItem addToCart(Long userId, Long bookId, int quantity) {
        logger.info("Adding item to cart - User ID: {}, Book ID: {}, Quantity: {}", userId, bookId, quantity);
        
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.warn("Add to cart failed - User not found with ID: {}", userId);
                        return new BusinessException("User not found");
                    });
            
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.warn("Add to cart failed - Book not found with ID: {}", bookId);
                        return new BusinessException("Book not found");
                    });
            
            // Check stock availability
            if (book.getStockQuantity() < quantity) {
                logger.warn("Add to cart failed - Insufficient stock. Available: {}, Requested: {}", 
                          book.getStockQuantity(), quantity);
                throw new BusinessException("Insufficient stock available");
            }
            
            Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndBookId(userId, bookId);
            
            CartItem cartItem;
            if (existingItem.isPresent()) {
                cartItem = existingItem.get();
                int newQuantity = cartItem.getQuantity() + quantity;
                
                // Check total quantity against stock
                if (book.getStockQuantity() < newQuantity) {
                    logger.warn("Add to cart failed - Total quantity {} exceeds stock {}", 
                              newQuantity, book.getStockQuantity());
                    throw new BusinessException("Total quantity exceeds available stock");
                }
                
                cartItem.setQuantity(newQuantity);
                logger.debug("Updated existing cart item - New quantity: {}", newQuantity);
            } else {
                cartItem = new CartItem(user, book, quantity);
                logger.debug("Created new cart item");
            }
            
            CartItem savedItem = cartItemRepository.save(cartItem);
            logger.info("Item added to cart successfully - Cart Item ID: {}", savedItem.getId());
            
            return savedItem;
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error adding item to cart - User ID: {}, Book ID: {}", userId, bookId, ex);
            throw new RuntimeException("Failed to add item to cart");
        }
    }
    
    @Transactional
    public void removeFromCart(Long userId, Long bookId) {
        logger.info("Removing item from cart - User ID: {}, Book ID: {}", userId, bookId);
        
        try {
            Optional<CartItem> cartItem = cartItemRepository.findByUserIdAndBookId(userId, bookId);
            if (cartItem.isEmpty()) {
                logger.warn("Remove from cart failed - Cart item not found for User ID: {}, Book ID: {}", userId, bookId);
                throw new BusinessException("Item not found in cart");
            }
            
            cartItemRepository.deleteByUserIdAndBookId(userId, bookId);
            logger.info("Item removed from cart successfully - User ID: {}, Book ID: {}", userId, bookId);
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error removing item from cart - User ID: {}, Book ID: {}", userId, bookId, ex);
            throw new RuntimeException("Failed to remove item from cart");
        }
    }
    
    public CartItem updateQuantity(Long userId, Long bookId, int quantity) {
        logger.info("Updating cart quantity - User ID: {}, Book ID: {}, New Quantity: {}", userId, bookId, quantity);
        
        try {
            if (quantity <= 0) {
                logger.info("Quantity is zero or negative, removing item from cart");
                removeFromCart(userId, bookId);
                return null;
            }
            
            CartItem cartItem = cartItemRepository.findByUserIdAndBookId(userId, bookId)
                    .orElseThrow(() -> {
                        logger.warn("Update quantity failed - Cart item not found for User ID: {}, Book ID: {}", userId, bookId);
                        return new BusinessException("Cart item not found");
                    });
            
            // Check stock availability
            if (cartItem.getBook().getStockQuantity() < quantity) {
                logger.warn("Update quantity failed - Insufficient stock. Available: {}, Requested: {}", 
                          cartItem.getBook().getStockQuantity(), quantity);
                throw new BusinessException("Insufficient stock available");
            }
            
            cartItem.setQuantity(quantity);
            CartItem savedItem = cartItemRepository.save(cartItem);
            
            logger.info("Cart quantity updated successfully - Cart Item ID: {}, New Quantity: {}", 
                       savedItem.getId(), quantity);
            
            return savedItem;
            
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error updating cart quantity - User ID: {}, Book ID: {}", userId, bookId, ex);
            throw new RuntimeException("Failed to update cart quantity");
        }
    }
    
    public List<CartItem> getCartItems(Long userId) {
        logger.debug("Fetching cart items for User ID: {}", userId);
        
        try {
            List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
            logger.debug("Found {} cart items for User ID: {}", cartItems.size(), userId);
            return cartItems;
            
        } catch (Exception ex) {
            logger.error("Unexpected error fetching cart items for User ID: {}", userId, ex);
            throw new RuntimeException("Failed to fetch cart items");
        }
    }
    
    public BigDecimal getCartTotal(Long userId) {
        logger.debug("Calculating cart total for User ID: {}", userId);
        
        try {
            List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
            BigDecimal total = cartItems.stream()
                    .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            logger.debug("Cart total calculated for User ID: {} - Total: {}", userId, total);
            return total;
            
        } catch (Exception ex) {
            logger.error("Unexpected error calculating cart total for User ID: {}", userId, ex);
            throw new RuntimeException("Failed to calculate cart total");
        }
    }
    
    @Transactional
    public void clearCart(Long userId) {
        logger.info("Clearing cart for User ID: {}", userId);
        
        try {
            cartItemRepository.deleteByUserId(userId);
            logger.info("Cart cleared successfully for User ID: {}", userId);
            
        } catch (Exception ex) {
            logger.error("Unexpected error clearing cart for User ID: {}", userId, ex);
            throw new RuntimeException("Failed to clear cart");
        }
    }
}