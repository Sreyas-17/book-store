package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Address;
import com.bookstore.bookstore_app.repository.AddressRepository;
import com.bookstore.bookstore_app.repository.UserRepository;
import com.bookstore.bookstore_app.service.AddressService;
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

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired 
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Address>> addAddress(@RequestBody Map<String, Object> addressData) {
        try {
            System.out.println("🏠 Adding address with data: " + addressData);

            // Extract user ID from the request
            Long userId = null;
            if (addressData.get("user") instanceof Map) {
                Map<String, Object> userMap = (Map<String, Object>) addressData.get("user");
                if (userMap.get("id") != null) {
                    userId = Long.valueOf(userMap.get("id").toString());
                }
            } else if (addressData.get("userId") != null) {
                userId = Long.valueOf(addressData.get("userId").toString());
            }

            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("User ID is required"));
            }

            System.out.println("🏠 User ID extracted: " + userId);

            // Find the user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("🏠 User found: " + user.getEmail());

            // Create new address
            Address address = new Address();
            address.setUser(user); // This is crucial!
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
            System.out.println("🏠 Address saved successfully with ID: " + savedAddress.getId());

            return ResponseEntity.ok(ApiResponse.success("Address added successfully", savedAddress));

        } catch (Exception e) {
            System.err.println("❌ Error adding address: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add address: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Address>>> getUserAddresses(@PathVariable Long userId) {
        try {
            List<Address> addresses = addressService.getUserAddresses(userId);
            return ResponseEntity.ok(ApiResponse.success("Addresses retrieved", addresses));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Address>> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        try {
            Address updatedAddress = addressService.updateAddress(id, address);
            return ResponseEntity.ok(ApiResponse.success("Address updated", updatedAddress));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok(ApiResponse.success("Address deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
