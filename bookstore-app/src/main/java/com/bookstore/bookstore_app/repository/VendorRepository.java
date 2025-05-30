package com.bookstore.bookstore_app.repository;

import com.bookstore.bookstore_app.entity.Review;
import com.bookstore.bookstore_app.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUserId(Long userId);
    List<Vendor> findByIsApprovedTrue();
    List<Vendor> findByIsApprovedFalse();

    long countByIsApprovedTrue();
    long countByIsApprovedFalse();

    @Query("SELECT r FROM Review r WHERE r.book.vendor.id = :vendorId")
    List<Review> findByVendorId(@Param("vendorId") Long vendorId);
}