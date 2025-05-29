package com.bookstore.bookstore_app.controller;

import com.bookstore.bookstore_app.dto.ApiResponse;
import com.bookstore.bookstore_app.entity.Address;
import com.bookstore.bookstore_app.service.AddressService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    @Test
    public void testGetUserAddresses() {
        Long userId = 1L;
        Address address = new Address();
        address.setId(1L);
        List<Address> addresses = Collections.singletonList(address);

        when(addressService.getUserAddresses(userId)).thenReturn(addresses);

        ResponseEntity<ApiResponse<List<Address>>> response = addressController.getUserAddresses(userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(1L, response.getBody().getData().get(0).getId());
    }

    @Test
    public void testAddAddress() {
        Map<String, Object> addressData = new HashMap<>();
        addressData.put("userId", 1L);
        addressData.put("addressLine1", "123 Main St");
        addressData.put("city", "Test City");
        addressData.put("state", "Test State");
        addressData.put("country", "Test Country");
        addressData.put("postalCode", "12345");

        Address address = new Address();
        address.setId(1L);
        when(addressService.addAddress(any())).thenReturn(address);

        ResponseEntity<ApiResponse<Address>> response = addressController.addAddress(addressData);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getData().getId());
    }

    @Test
    public void testUpdateAddress() {
        Long id = 1L;
        Address address = new Address();
        address.setId(id);
        when(addressService.updateAddress(id, address)).thenReturn(address);

        ResponseEntity<ApiResponse<Address>> response = addressController.updateAddress(id, address);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(id, response.getBody().getData().getId());
    }

    @Test
    public void testDeleteAddress() {
        Long id = 1L;
        doNothing().when(addressService).deleteAddress(id);

        ResponseEntity<ApiResponse<String>> response = addressController.deleteAddress(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Address deleted successfully", response.getBody().getMessage());
    }
} 
 