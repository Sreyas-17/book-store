package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Wishlist;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.repository.WishlistRepository;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public Wishlist addToWishlist(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<Wishlist> existing = wishlistRepository.findByUserIdAndBookId(userId, bookId);
        if (existing.isPresent()) {
            throw new RuntimeException("Book already in wishlist");
        }

        Wishlist wishlist = new Wishlist(user, book);
        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long bookId) {
        wishlistRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    public List<Wishlist> getUserWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @Transactional
    public void moveToCart(Long userId, Long bookId) {
        cartService.addToCart(userId, bookId, 1);
        wishlistRepository.deleteByUserIdAndBookId(userId, bookId);
    }
}