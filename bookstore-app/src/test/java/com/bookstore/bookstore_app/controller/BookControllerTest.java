package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.service.BookService;
import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;


    @Test
    public void testAddBook() {
        Book book = new Book();
        book.setTitle("New Book");
        when(bookService.addBook(any())).thenReturn(book);

        ResponseEntity<ApiResponse<Book>> response = bookController.addBook(book);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("New Book", response.getBody().getData().getTitle());
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = Collections.singletonList(new Book());
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<ApiResponse<List<Book>>> response = bookController.getAllBooks();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testSearchByTitle() {
        String title = "Test Title";
        List<Book> books = Collections.singletonList(new Book());
        when(bookService.searchByTitle(title)).thenReturn(books);

        ResponseEntity<ApiResponse<List<Book>>> response = bookController.searchByTitle(title);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testSearchByAuthor() {
        String author = "Test Author";
        List<Book> books = Collections.singletonList(new Book());
        when(bookService.searchByAuthor(author)).thenReturn(books);

        ResponseEntity<ApiResponse<List<Book>>> response = bookController.searchByAuthor(author);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testSearchByRating() {
        Double rating = 4.5;
        List<Book> books = Collections.singletonList(new Book());
        when(bookService.searchByRating(rating)).thenReturn(books);

        ResponseEntity<ApiResponse<List<Book>>> response = bookController.searchByRating(rating);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    public void testGetBooksWithPagination() {
        int page = 0;
        int size = 10;
        Page<Book> books = mock(Page.class);
        when(bookService.getBooksWithPagination(page, size)).thenReturn(books);

        ResponseEntity<ApiResponse<Page<Book>>> response = bookController.getBooksWithPagination(page, size);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetBooksSortedByPriceAsc() {
        int page = 0;
        int size = 10;
        Page<Book> books = mock(Page.class);
        when(bookService.getBooksSortedByPriceAsc(page, size)).thenReturn(books);

        ResponseEntity<ApiResponse<Page<Book>>> response = bookController.getBooksSortedByPriceAsc(page, size);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetBooksSortedByPriceDesc() {
        int page = 0;
        int size = 10;
        Page<Book> books = mock(Page.class);
        when(bookService.getBooksSortedByPriceDesc(page, size)).thenReturn(books);

        ResponseEntity<ApiResponse<Page<Book>>> response = bookController.getBooksSortedByPriceDesc(page, size);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testGetNewArrivals() {
        int page = 0;
        int size = 10;
        Page<Book> books = mock(Page.class);
        when(bookService.getNewArrivals(page, size)).thenReturn(books);

        ResponseEntity<ApiResponse<Page<Book>>> response = bookController.getNewArrivals(page, size);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testUpdateBookImage() {
        Long id = 1L;
        String imageUrl = "http://example.com/image.jpg";
        Book book = new Book();
        book.setId(id);
        when(bookService.updateBookImage(id, imageUrl)).thenReturn(book);

        ResponseEntity<ApiResponse<Book>> response = bookController.updateBookImage(id, imageUrl);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    public void testRateBook() {
        Long bookId = 1L;
        Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("userId", 1L);
        ratingData.put("rating", 5);
        Book book = new Book();
        book.setId(bookId);
        when(bookService.rateBook(bookId, 5)).thenReturn(book);

        ResponseEntity<ApiResponse<String>> response = bookController.rateBook(bookId, ratingData);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Book rated successfully", response.getBody().getMessage());
    }
} 