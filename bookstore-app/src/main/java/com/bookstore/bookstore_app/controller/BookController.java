package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private static final Logger logger = LogManager.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
        logger.info("POST /api/books/add - Adding new book: {}", book.getTitle());
        
        try {
            Book savedBook = bookService.addBook(book);
            logger.info("Book added successfully via API - ID: {}, Title: {}", savedBook.getId(), savedBook.getTitle());
            return ResponseEntity.ok(ApiResponse.success("Book added successfully", savedBook));
        } catch (Exception e) {
            logger.error("Error adding book via API: {} - Error: {}", book.getTitle(), e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        logger.info("GET /api/books/all - Fetching all books");
        
        try {
            List<Book> books = bookService.getAllBooks();
            logger.info("API returned {} books", books.size());
            return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", books));
        } catch (Exception e) {
            logger.error("Error fetching all books via API - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<List<Book>>> searchByTitle(@RequestParam String title) {
        logger.info("GET /api/books/search/title - Searching by title: {}", title);
        
        try {
            List<Book> books = bookService.searchByTitle(title);
            logger.info("API search by title returned {} books for: {}", books.size(), title);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            logger.error("Error searching books by title: {} - Error: {}", title, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search/author")
    public ResponseEntity<ApiResponse<List<Book>>> searchByAuthor(@RequestParam String author) {
        logger.info("GET /api/books/search/author - Searching by author: {}", author);
        
        try {
            List<Book> books = bookService.searchByAuthor(author);
            logger.info("API search by author returned {} books for: {}", books.size(), author);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            logger.error("Error searching books by author: {} - Error: {}", author, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search/rating")
    public ResponseEntity<ApiResponse<List<Book>>> searchByRating(@RequestParam Double rating) {
        logger.info("GET /api/books/search/rating - Searching by rating >= {}", rating);
        
        try {
            List<Book> books = bookService.searchByRating(rating);
            logger.info("API search by rating returned {} books with rating >= {}", books.size(), rating);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            logger.error("Error searching books by rating: {} - Error: {}", rating, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/paginated - Page: {}, Size: {}", page, size);
        
        try {
            Page<Book> books = bookService.getBooksWithPagination(page, size);
            logger.info("API pagination returned {} books on page {} of {}", 
                       books.getNumberOfElements(), page, books.getTotalPages());
            return ResponseEntity.ok(ApiResponse.success("Books retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching paginated books - Page: {}, Size: {} - Error: {}", 
                        page, size, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/sort/price-asc")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksSortedByPriceAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/sort/price-asc - Page: {}, Size: {}", page, size);
        
        try {
            Page<Book> books = bookService.getBooksSortedByPriceAsc(page, size);
            logger.info("API price sort (asc) returned {} books on page {}", books.getNumberOfElements(), page);
            return ResponseEntity.ok(ApiResponse.success("Books sorted by price (ascending)", books));
        } catch (Exception e) {
            logger.error("Error fetching books sorted by price (asc) - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/sort/price-desc")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksSortedByPriceDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/sort/price-desc - Page: {}, Size: {}", page, size);
        
        try {
            Page<Book> books = bookService.getBooksSortedByPriceDesc(page, size);
            logger.info("API price sort (desc) returned {} books on page {}", books.getNumberOfElements(), page);
            return ResponseEntity.ok(ApiResponse.success("Books sorted by price (descending)", books));
        } catch (Exception e) {
            logger.error("Error fetching books sorted by price (desc) - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<Page<Book>>> getNewArrivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/new-arrivals - Page: {}, Size: {}", page, size);
        
        try {
            Page<Book> books = bookService.getNewArrivals(page, size);
            logger.info("API new arrivals returned {} books on page {}", books.getNumberOfElements(), page);
            return ResponseEntity.ok(ApiResponse.success("New arrivals retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching new arrivals - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<ApiResponse<Book>> updateBookImage(@PathVariable Long id, @RequestParam String imageUrl) {
        logger.info("PUT /api/books/{}/image - Updating book image", id);
        logger.debug("New image URL: {}", imageUrl);
        
        try {
            Book book = bookService.updateBookImage(id, imageUrl);
            logger.info("Book image updated successfully via API - Book ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success("Book image updated", book));
        } catch (Exception e) {
            logger.error("Error updating book image via API - Book ID: {} - Error: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{bookId}/rate")
    public ResponseEntity<ApiResponse<String>> rateBook(
            @PathVariable Long bookId,
            @RequestBody Map<String, Object> ratingData) {
        logger.info("POST /api/books/{}/rate - Rating book", bookId);
        logger.debug("Rating data: {}", ratingData);
        
        try {
            Long userId = Long.valueOf(ratingData.get("userId").toString());
            Integer rating = Integer.valueOf(ratingData.get("rating").toString());
            
            logger.info("User ID: {}, Rating: {} stars for book ID: {}", userId, rating, bookId);
            
            // Validate rating
            if (rating < 1 || rating > 5) {
                logger.warn("Invalid rating attempted: {} for book ID: {}", rating, bookId);
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Rating must be between 1 and 5 stars"));
            }
            
            Book updatedBook = bookService.rateBook(bookId, rating);
            
            logger.info("Book rated successfully via API - Book: {}, New Rating: {}", 
                       updatedBook.getTitle(), updatedBook.getRatingAvg());
            
            return ResponseEntity.ok(ApiResponse.success("Book rated successfully"));
            
        } catch (Exception e) {
            logger.error("Error rating book via API - Book ID: {} - Error: {}", bookId, e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to rate book: " + e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Book>>> getBooksByCategory(@PathVariable Long categoryId) {
        logger.info("GET /api/books/category/{} - Fetching books by category", categoryId);

        try {
            List<Book> books = bookService.getBooksByCategory(categoryId);
            logger.info("Retrieved {} books for category ID: {}", books.size(), categoryId);
            return ResponseEntity.ok(ApiResponse.success("Books retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching books by category - Category ID: {} - Error: {}", categoryId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }


}
