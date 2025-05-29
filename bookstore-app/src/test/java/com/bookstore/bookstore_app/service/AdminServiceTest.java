package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Book;
import com.bookstore.bookstore_app.entity.Review;
import com.bookstore.bookstore_app.entity.Vendor;
import com.bookstore.bookstore_app.repository.BookRepository;
import com.bookstore.bookstore_app.repository.ReviewRepository;
import com.bookstore.bookstore_app.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private VendorRepository vendorRepository;

    private Book book;
    private Review review;
    private Vendor vendor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setApproved(false);

        review = new Review();
        review.setId(1L);
        review.setApproved(false);

        vendor = new Vendor();
        vendor.setId(1L);
    }

    @Test
    void approveBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = adminService.approveBook(1L);

        assertNotNull(result);
        assertTrue(result.isApproved());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void approveBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            adminService.approveBook(1L);
        });

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void disapproveBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = adminService.disapproveBook(1L);

        assertNotNull(result);
        assertFalse(result.isApproved());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void approveReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = adminService.approveReview(1L);

        assertNotNull(result);
        assertTrue(result.isApproved());
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void approveReview_NotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            adminService.approveReview(1L);
        });

        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void getPendingBooks_Success() {
        List<Book> expectedBooks = Arrays.asList(book);
        when(bookRepository.findByIsApprovedFalse()).thenReturn(expectedBooks);

        List<Book> result = adminService.getPendingBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getId(), result.get(0).getId());
        verify(bookRepository, times(1)).findByIsApprovedFalse();
    }

    @Test
    void getAllBooks_Success() {
        List<Book> expectedBooks = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = adminService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(book.getId(), result.get(0).getId());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getAllVendors_Success() {
        List<Vendor> expectedVendors = Arrays.asList(vendor);
        when(vendorRepository.findAll()).thenReturn(expectedVendors);

        List<Vendor> result = adminService.getAllVendors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vendor.getId(), result.get(0).getId());
        verify(vendorRepository, times(1)).findAll();
    }
} 