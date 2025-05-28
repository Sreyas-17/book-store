package com.bookstore.bookstore_app.repository;

import com.bookstore.bookstore_app.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE b.ratingAvg >= :rating")
    List<Book> findByRatingGreaterThanEqual(@Param("rating") Double rating);

    Page<Book> findByOrderByPriceAsc(Pageable pageable);
    Page<Book> findByOrderByPriceDesc(Pageable pageable);
    Page<Book> findByOrderByCreatedAtDesc(Pageable pageable);

    List<Book> findByIsApprovedTrue();
    List<Book> findByIsApprovedFalse();
}