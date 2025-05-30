package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setDescription("Test Description");
        book.setPrice(new BigDecimal("19.99"));
        book.setStockQuantity(10);
        book.setIsbn("1234567890");
        book.setRatingAvg(new BigDecimal("4.5"));
        book.setTotalRatings(10);
    }

    @Test
    void addBook_Success() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_Success() {
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPrice(new BigDecimal("29.99"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals(updatedBook.getTitle(), result.getTitle());
        assertEquals(updatedBook.getAuthor(), result.getAuthor());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            bookService.updateBook(1L, book);
        });

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            bookService.deleteBook(1L);
        });

        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllBooks_Success() {
        List<Book> expectedBooks = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getId(), result.get(0).getId());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void searchByTitle_Success() {
        List<Book> expectedBooks = Arrays.asList(book);
        when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(expectedBooks);

        List<Book> result = bookService.searchByTitle("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getTitle(), result.get(0).getTitle());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    void searchByAuthor_Success() {
        List<Book> expectedBooks = Arrays.asList(book);
        when(bookRepository.findByAuthorContainingIgnoreCase("Test")).thenReturn(expectedBooks);

        List<Book> result = bookService.searchByAuthor("Test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getAuthor(), result.get(0).getAuthor());
        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("Test");
    }

    @Test
    void getBooksWithPagination_Success() {
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<Book> result = bookService.getBooksWithPagination(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(book.getId(), result.getContent().get(0).getId());
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void rateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.rateBook(1L, 5);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBookImage_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.updateBookImage(1L, "new-image-url.jpg");

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
} 