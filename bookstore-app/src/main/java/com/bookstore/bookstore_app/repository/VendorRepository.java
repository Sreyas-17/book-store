package com.bookstore.bookstore_app.repository;

import com.bookstore.bookstore_app.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUserId(Long userId);
    List<Vendor> findByIsApprovedTrue();
    List<Vendor> findByIsApprovedFalse();
}