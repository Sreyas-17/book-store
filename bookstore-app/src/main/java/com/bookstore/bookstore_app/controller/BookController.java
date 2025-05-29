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
@CrossOrigin(origins = "*") // Allow requests from any origin
public class BookController {

    private static final Logger logger = LogManager.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    /**
     * Adds a new book to the system.
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
        logger.info("POST /api/books/add - Adding new book: {}", book.getTitle());

        try {
            Book savedBook = bookService.addBook(book);
            return ResponseEntity.ok(ApiResponse.success("Book added successfully", savedBook));
        } catch (Exception e) {
            logger.error("Error adding book - Title: {} - {}", book.getTitle(), e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Fetches all available books.
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        logger.info("GET /api/books/all - Fetching all books");

        try {
            List<Book> books = bookService.getAllBooks();
            return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", books));
        } catch (Exception e) {
            logger.error("Error fetching books - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Searches books by title.
     */
    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<List<Book>>> searchByTitle(@RequestParam String title) {
        logger.info("GET /api/books/search/title - Searching by title: {}", title);

        try {
            List<Book> books = bookService.searchByTitle(title);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            logger.error("Error searching books by title - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Searches books by author name.
     */
    @GetMapping("/search/author")
    public ResponseEntity<ApiResponse<List<Book>>> searchByAuthor(@RequestParam String author) {
        logger.info("GET /api/books/search/author - Searching by author: {}", author);

        try {
            List<Book> books = bookService.searchByAuthor(author);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            logger.error("Error searching books by author - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Searches books by minimum rating.
     */
    @GetMapping("/search/rating")
    public ResponseEntity<ApiResponse<List<Book>>> searchByRating(@RequestParam Double rating) {
        logger.info("GET /api/books/search/rating - Searching books with rating >= {}", rating);

        try {
            List<Book> books = bookService.searchByRating(rating);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            logger.error("Error searching books by rating - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves books with pagination.
     */
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/paginated - Page: {}, Size: {}", page, size);

        try {
            Page<Book> books = bookService.getBooksWithPagination(page, size);
            return ResponseEntity.ok(ApiResponse.success("Books retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching paginated books - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves books sorted by price in ascending order.
     */
    @GetMapping("/sort/price-asc")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksSortedByPriceAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/sort/price-asc - Page: {}, Size: {}", page, size);

        try {
            Page<Book> books = bookService.getBooksSortedByPriceAsc(page, size);
            return ResponseEntity.ok(ApiResponse.success("Books sorted by price (ascending)", books));
        } catch (Exception e) {
            logger.error("Error fetching books sorted by price - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves books sorted by price in descending order.
     */
    @GetMapping("/sort/price-desc")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksSortedByPriceDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/sort/price-desc - Page: {}, Size: {}", page, size);

        try {
            Page<Book> books = bookService.getBooksSortedByPriceDesc(page, size);
            return ResponseEntity.ok(ApiResponse.success("Books sorted by price (descending)", books));
        } catch (Exception e) {
            logger.error("Error fetching books sorted by price - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Retrieves recently added books.
     */
    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<Page<Book>>> getNewArrivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /api/books/new-arrivals - Page: {}, Size: {}", page, size);

        try {
            Page<Book> books = bookService.getNewArrivals(page, size);
            return ResponseEntity.ok(ApiResponse.success("New arrivals retrieved", books));
        } catch (Exception e) {
            logger.error("Error fetching new arrivals - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Updates the image of a book.
     */
    @PutMapping("/{id}/image")
    public ResponseEntity<ApiResponse<Book>> updateBookImage(@PathVariable Long id, @RequestParam String imageUrl) {
        logger.info("PUT /api/books/{}/image - Updating book image", id);

        try {
            Book book = bookService.updateBookImage(id, imageUrl);
            return ResponseEntity.ok(ApiResponse.success("Book image updated", book));
        } catch (Exception e) {
            logger.error("Error updating book image - {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
