package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Address;
import com.bookstore.bookstore_app.repository.AddressRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressService {

    private static final Logger logger = LogManager.getLogger(AddressService.class);

    @Autowired
    private AddressRepository addressRepository;

    public Address addAddress(Address address) {
        logger.info("Adding new address for user ID: {}", address.getUser().getId());
        try {
            Address savedAddress = addressRepository.save(address);
            logger.info("Address added successfully with ID: {}", savedAddress.getId());
            return savedAddress;
        } catch (Exception e) {
            logger.error("Failed to add address for user ID: {} - Error: {}", address.getUser().getId(), e.getMessage(), e);
            throw e;
        }
    }

    public List<Address> getUserAddresses(Long userId) {
        logger.debug("Fetching addresses for user ID: {}", userId);
        try {
            List<Address> addresses = addressRepository.findByUserId(userId);
            logger.info("Found {} addresses for user ID: {}", addresses.size(), userId);
            return addresses;
        } catch (Exception e) {
            logger.error("Failed to fetch addresses for user ID: {} - Error: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    public Address updateAddress(Long id, Address updatedAddress) {
        logger.info("Updating address with ID: {}", id);
        try {
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Address not found for update with ID: {}", id);
                        return new RuntimeException("Address not found");
                    });

            address.setAddressLine1(updatedAddress.getAddressLine1());
            address.setAddressLine2(updatedAddress.getAddressLine2());
            address.setCity(updatedAddress.getCity());
            address.setState(updatedAddress.getState());
            address.setCountry(updatedAddress.getCountry());
            address.setPostalCode(updatedAddress.getPostalCode());
            address.setDefault(updatedAddress.isDefault());

            Address savedAddress = addressRepository.save(address);
            logger.info("Address updated successfully with ID: {}", savedAddress.getId());
            return savedAddress;
        } catch (Exception e) {
            logger.error("Failed to update address with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void deleteAddress(Long id) {
        logger.info("Deleting address with ID: {}", id);
        try {
            if (!addressRepository.existsById(id)) {
                logger.warn("Cannot delete - Address not found with ID: {}", id);
                throw new RuntimeException("Address not found");
            }
            addressRepository.deleteById(id);
            logger.info("Address deleted successfully with ID: {}", id);
        } catch (Exception e) {
            logger.error("Failed to delete address with ID: {} - Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}