package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Book>> addBook(@RequestBody Book book) {
        try {
            Book savedBook = bookService.addBook(book);
            return ResponseEntity.ok(ApiResponse.success("Book added successfully", savedBook));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<List<Book>>> searchByTitle(@RequestParam String title) {
        try {
            List<Book> books = bookService.searchByTitle(title);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search/author")
    public ResponseEntity<ApiResponse<List<Book>>> searchByAuthor(@RequestParam String author) {
        try {
            List<Book> books = bookService.searchByAuthor(author);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/search/rating")
    public ResponseEntity<ApiResponse<List<Book>>> searchByRating(@RequestParam Double rating) {
        try {
            List<Book> books = bookService.searchByRating(rating);
            return ResponseEntity.ok(ApiResponse.success("Books found", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Book> books = bookService.getBooksWithPagination(page, size);
            return ResponseEntity.ok(ApiResponse.success("Books retrieved", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/sort/price-asc")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksSortedByPriceAsc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Book> books = bookService.getBooksSortedByPriceAsc(page, size);
            return ResponseEntity.ok(ApiResponse.success("Books sorted by price (ascending)", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/sort/price-desc")
    public ResponseEntity<ApiResponse<Page<Book>>> getBooksSortedByPriceDesc(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Book> books = bookService.getBooksSortedByPriceDesc(page, size);
            return ResponseEntity.ok(ApiResponse.success("Books sorted by price (descending)", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<Page<Book>>> getNewArrivals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Book> books = bookService.getNewArrivals(page, size);
            return ResponseEntity.ok(ApiResponse.success("New arrivals retrieved", books));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<ApiResponse<Book>> updateBookImage(@PathVariable Long id, @RequestParam String imageUrl) {
        try {
            Book book = bookService.updateBookImage(id, imageUrl);
            return ResponseEntity.ok(ApiResponse.success("Book image updated", book));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}