package com.bookstore.bookstore_app.repository;

import com.bookstore.bookstore_app.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByUserId(Long userId);
    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);
    List<Review> findByIsApprovedTrue();
    List<Review> findByIsApprovedFalse();
}
