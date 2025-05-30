package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.repository.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        logger.info("Adding new book: {}", book.getTitle());
        try {
            Book savedBook = bookRepository.save(book);
            logger.info("Book added successfully with ID: {} - Title: {}", savedBook.getId(), savedBook.getTitle());
            return savedBook;
        } catch (Exception e) {
            logger.error("Failed to add book: {} - Error: {}", book.getTitle(), e.getMessage(), e);
            throw e;
        }
    }

    public Book updateBook(Long id, Book updatedBook) {
        logger.info("Updating book with ID: {}", id);
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Book not found for update with ID: {}", id);
                        return new RuntimeException("Book not found");
                    });

            logger.debug("Original book details - Title: {}, Author: {}", book.getTitle(), book.getAuthor());

            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setDescription(updatedBook.getDescription());
            book.setPrice(updatedBook.getPrice());
            book.setStockQuantity(updatedBook.getStockQuantity());
            book.setImageUrl(updatedBook.getImageUrl());

            Book savedBook = bookRepository.save(book);
            logger.info("Book updated successfully - ID: {}, Title: {}", savedBook.getId(), savedBook.getTitle());
            return savedBook;
        } catch (Exception e) {
            logger.error("Failed to update book with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        try {
            if (!bookRepository.existsById(id)) {
                logger.warn("Cannot delete - Book not found with ID: {}", id);
                throw new RuntimeException("Book not found");
            }
            bookRepository.deleteById(id);
            logger.info("Book deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete book with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> getAllBooks() {
        logger.debug("Fetching all books from database");
        try {
            List<Book> books = bookRepository.findAll();
            logger.info("Retrieved {} books from database", books.size());
            return books;
        } catch (Exception e) {
            logger.error("Failed to fetch all books - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Book getBookById(Long id) {
        logger.debug("Fetching book by ID: {}", id);
        try {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Book not found with ID: {}", id);
                        return new RuntimeException("Book not found");
                    });
            logger.debug("Found book: {} by {}", book.getTitle(), book.getAuthor());
            return book;
        } catch (Exception e) {
            logger.error("Failed to fetch book with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> searchByTitle(String title) {
        logger.info("Searching books by title: {}", title);
        try {
            List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
            logger.info("Found {} books matching title: {}", books.size(), title);
            return books;
        } catch (Exception e) {
            logger.error("Failed to search books by title: {} - Error: {}", title, e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> searchByAuthor(String author) {
        logger.info("Searching books by author: {}", author);
        try {
            List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
            logger.info("Found {} books by author: {}", books.size(), author);
            return books;
        } catch (Exception e) {
            logger.error("Failed to search books by author: {} - Error: {}", author, e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> searchByIsbn(String isbn) {
        logger.info("Searching books by ISBN: {}", isbn);
        try {
            List<Book> books = bookRepository.findByIsbn(isbn);
            logger.info("Found {} books with ISBN: {}", books.size(), isbn);
            return books;
        } catch (Exception e) {
            logger.error("Failed to search books by ISBN: {} - Error: {}", isbn, e.getMessage(), e);
            throw e;
        }
    }

    public List<Book> searchByRating(Double rating) {
        logger.info("Searching books with rating >= {}", rating);
        try {
            List<Book> books = bookRepository.findByRatingGreaterThanEqual(rating);
            logger.info("Found {} books with rating >= {}", books.size(), rating);
            return books;
        } catch (Exception e) {
            logger.error("Failed to search books by rating: {} - Error: {}", rating, e.getMessage(), e);
            throw e;
        }
    }

    public Page<Book> getBooksWithPagination(int page, int size) {
        logger.debug("Fetching books with pagination - Page: {}, Size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> books = bookRepository.findAll(pageable);
            logger.info("Retrieved {} books on page {} (Total: {})", books.getNumberOfElements(), page, books.getTotalElements());
            return books;
        } catch (Exception e) {
            logger.error("Failed to fetch books with pagination - Page: {}, Size: {} - Error: {}", page, size, e.getMessage(), e);
            throw e;
        }
    }

    public Page<Book> getBooksSortedByPriceAsc(int page, int size) {
        logger.debug("Fetching books sorted by price (ascending) - Page: {}, Size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> books = bookRepository.findByOrderByPriceAsc(pageable);
            logger.info("Retrieved {} books sorted by price (asc) on page {}", books.getNumberOfElements(), page);
            return books;
        } catch (Exception e) {
            logger.error("Failed to fetch books sorted by price (asc) - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Page<Book> getBooksSortedByPriceDesc(int page, int size) {
        logger.debug("Fetching books sorted by price (descending) - Page: {}, Size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> books = bookRepository.findByOrderByPriceDesc(pageable);
            logger.info("Retrieved {} books sorted by price (desc) on page {}", books.getNumberOfElements(), page);
            return books;
        } catch (Exception e) {
            logger.error("Failed to fetch books sorted by price (desc) - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Page<Book> getNewArrivals(int page, int size) {
        logger.debug("Fetching new arrivals - Page: {}, Size: {}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> books = bookRepository.findByOrderByCreatedAtDesc(pageable);
            logger.info("Retrieved {} new arrivals on page {}", books.getNumberOfElements(), page);
            return books;
        } catch (Exception e) {
            logger.error("Failed to fetch new arrivals - Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Book updateBookImage(Long id, String imageUrl) {
        logger.info("Updating image for book ID: {} - New URL: {}", id, imageUrl);
        try {
            Book book = getBookById(id);
            book.setImageUrl(imageUrl);
            Book savedBook = bookRepository.save(book);
            logger.info("Book image updated successfully for ID: {}", id);
            return savedBook;
        } catch (Exception e) {
            logger.error("Failed to update book image for ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public Book rateBook(Long bookId, Integer rating) {
        logger.info("Rating book ID: {} with {} stars", bookId, rating);
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> {
                        logger.error("Book not found for rating with ID: {}", bookId);
                        return new RuntimeException("Book not found with id: " + bookId);
                    });

            logger.debug("Found book for rating: {}", book.getTitle());
            logger.debug("Current rating: {} (Total ratings: {})", book.getRatingAvg(), book.getTotalRatings());

            BigDecimal currentTotalPoints = book.getRatingAvg()
                    .multiply(BigDecimal.valueOf(book.getTotalRatings()));

            BigDecimal newTotalPoints = currentTotalPoints.add(BigDecimal.valueOf(rating));
            int newTotalRatings = book.getTotalRatings() + 1;

            BigDecimal newAverage = newTotalPoints.divide(
                    BigDecimal.valueOf(newTotalRatings),
                    2,
                    RoundingMode.HALF_UP
            );

            book.setRatingAvg(newAverage);
            book.setTotalRatings(newTotalRatings);

            Book savedBook = bookRepository.save(book);

            logger.info("Book rated successfully - New average: {}, Total ratings: {}", newAverage, newTotalRatings);
            return savedBook;

        } catch (Exception e) {
            logger.error("Error rating book ID: {} - Error: {}", bookId, e.getMessage(), e);
            throw new RuntimeException("Failed to rate book: " + e.getMessage(), e);
        }
    }

    public List<Book> getBooksByCategory(Long categoryId) {
        logger.info("Fetching books for category ID: {}", categoryId);
        try {
            List<Book> books = bookRepository.findByCategoryId(categoryId);
            logger.info("Found {} books for category ID: {}", books.size(), categoryId);
            return books;
        } catch (Exception e) {
            logger.error("Failed to fetch books by category ID: {} - Error: {}", categoryId, e.getMessage(), e);
            throw e;
        }
    }
}
