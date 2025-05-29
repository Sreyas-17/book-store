package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Address;
import com.bookstore.bookstore_app.repository.AddressRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import com.bookstore.bookstore_app.service.AddressService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookstore.bookstore_app.entity.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    private static final Logger logger = LogManager.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired 
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Address>> addAddress(@RequestBody Map<String, Object> addressData) {
        logger.info("POST /api/addresses/add - Adding new address");
        logger.debug("Address data received: {}", addressData);
        
        try {
            // Extract user ID from the request
            Long userId = extractUserIdFromRequest(addressData);

            if (userId == null) {
                logger.warn("Address creation failed - User ID is required");
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User ID is required"));
            }

            logger.debug("User ID extracted: {}", userId);

            // Create final variable for lambda
            final Long finalUserId = userId;

            // Find the user
            User user = userRepository.findById(finalUserId)
                    .orElseThrow(() -> {
                        logger.error("Address creation failed - User not found with ID: {}", finalUserId);
                        return new RuntimeException("User not found");
                    });

            logger.debug("User found: {}", user.getEmail());

            // Create new address
            Address address = new Address();
            address.setUser(user);
            address.setAddressLine1(addressData.get("addressLine1").toString());

            if (addressData.get("addressLine2") != null) {
                address.setAddressLine2(addressData.get("addressLine2").toString());
            }

            address.setCity(addressData.get("city").toString());
            address.setState(addressData.get("state").toString());
            address.setCountry(addressData.get("country").toString());
            address.setPostalCode(addressData.get("postalCode").toString());

            if (addressData.get("default") != null) {
                address.setDefault(Boolean.valueOf(addressData.get("default").toString()));
            }

            // Save address
            Address savedAddress = addressRepository.save(address);
            logger.info("Address added successfully with ID: {} for user: {}", savedAddress.getId(), user.getEmail());

            return ResponseEntity.ok(ApiResponse.success("Address added successfully", savedAddress));

        } catch (Exception e) {
            logger.error("Error adding address - Error: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add address: " + e.getMessage()));
        }
    }

    // Helper method to extract user ID
    private Long extractUserIdFromRequest(Map<String, Object> addressData) {
        if (addressData.get("user") instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> userMap = (Map<String, Object>) addressData.get("user");
            if (userMap.get("id") != null) {
                return Long.valueOf(userMap.get("id").toString());
            }
        } else if (addressData.get("userId") != null) {
            return Long.valueOf(addressData.get("userId").toString());
        }
        return null;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Address>>> getUserAddresses(@PathVariable Long userId) {
        logger.info("GET /api/addresses/user/{} - Fetching user addresses", userId);
        
        try {
            List<Address> addresses = addressService.getUserAddresses(userId);
            logger.info("Retrieved {} addresses for user ID: {}", addresses.size(), userId);
            return ResponseEntity.ok(ApiResponse.success("Addresses retrieved", addresses));
        } catch (Exception e) {
            logger.error("Error fetching addresses for user ID: {} - Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        logger.info("PUT /api/addresses/{} - Updating address", id);
        
        try {
            Address updatedAddress = addressService.updateAddress(id, address);
            logger.info("Address updated successfully with ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success("Address updated", updatedAddress));
        } catch (Exception e) {
            logger.error("Error updating address with ID: {} - Error: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        logger.info("DELETE /api/addresses/{} - Deleting address", id);
        
        try {
            addressService.deleteAddress(id);
            logger.info("Address deleted successfully with ID: {}", id);
            return ResponseEntity.ok(ApiResponse.success("Address deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting address with ID: {} - Error: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}