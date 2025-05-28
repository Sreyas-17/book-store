package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setDescription(updatedBook.getDescription());
        book.setPrice(updatedBook.getPrice());
        book.setStockQuantity(updatedBook.getStockQuantity());
        book.setImageUrl(updatedBook.getImageUrl());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> searchByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> searchByRating(Double rating) {
        return bookRepository.findByRatingGreaterThanEqual(rating);
    }

    public Page<Book> getBooksWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    public Page<Book> getBooksSortedByPriceAsc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByOrderByPriceAsc(pageable);
    }

    public Page<Book> getBooksSortedByPriceDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByOrderByPriceDesc(pageable);
    }

    public Page<Book> getNewArrivals(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByOrderByCreatedAtDesc(pageable);
    }

    public Book updateBookImage(Long id, String imageUrl) {
        Book book = getBookById(id);
        book.setImageUrl(imageUrl);
        return bookRepository.save(book);
    }

    public Book rateBook(Long bookId, Integer rating) {
    try {
        System.out.println("📚 BookService: Rating book " + bookId + " with " + rating + " stars");
        
        // Find the book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        
        System.out.println("📚 Found book: " + book.getTitle());
        System.out.println("📚 Current rating: " + book.getRatingAvg() + " (Total ratings: " + book.getTotalRatings() + ")");
        
        // Calculate new average rating
        BigDecimal currentTotalPoints = book.getRatingAvg()
                .multiply(BigDecimal.valueOf(book.getTotalRatings()));
        
        BigDecimal newTotalPoints = currentTotalPoints.add(BigDecimal.valueOf(rating));
        int newTotalRatings = book.getTotalRatings() + 1;
        
        BigDecimal newAverage = newTotalPoints.divide(
                BigDecimal.valueOf(newTotalRatings), 
                2, 
                RoundingMode.HALF_UP
        );
        
        // Update book
        book.setRatingAvg(newAverage);
        book.setTotalRatings(newTotalRatings);
        
        // Save the book
        Book savedBook = bookRepository.save(book);
        
        System.out.println("📚 Book rated successfully - New average: " + newAverage + ", Total ratings: " + newTotalRatings);
        
        return savedBook;
        
    } catch (Exception e) {
        System.err.println("❌ BookService: Error rating book: " + e.getMessage());
        throw new RuntimeException("Failed to rate book: " + e.getMessage(), e);
    }
}
}