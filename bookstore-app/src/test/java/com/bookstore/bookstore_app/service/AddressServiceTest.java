package com.bookstore.bookstore_app.service;

import com.bookstore.bookstore_app.entity.Address;
import com.bookstore.bookstore_app.entity.User;
import com.bookstore.bookstore_app.repository.AddressRepository;
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

class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    private Address address;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup test data
        user = new User();
        user.setId(1L);
        
        address = new Address();
        address.setId(1L);
        address.setUser(user);
        address.setAddressLine1("123 Main St");
        address.setAddressLine2("Apt 4B");
        address.setCity("New York");
        address.setState("NY");
        address.setCountry("USA");
        address.setPostalCode("10001");
        address.setDefault(true);
    }

    @Test
    void addAddress_Success() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.addAddress(address);

        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getAddressLine1(), result.getAddressLine1());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void getUserAddresses_Success() {
        List<Address> expectedAddresses = Arrays.asList(address);
        when(addressRepository.findByUserId(1L)).thenReturn(expectedAddresses);

        List<Address> result = addressService.getUserAddresses(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(address.getId(), result.get(0).getId());
        verify(addressRepository, times(1)).findByUserId(1L);
    }

    @Test
    void updateAddress_Success() {
        Address updatedAddress = new Address();
        updatedAddress.setAddressLine1("456 New St");
        updatedAddress.setAddressLine2("Suite 5C");
        updatedAddress.setCity("Boston");
        updatedAddress.setState("MA");
        updatedAddress.setCountry("USA");
        updatedAddress.setPostalCode("02108");
        updatedAddress.setDefault(false);

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

        Address result = addressService.updateAddress(1L, updatedAddress);

        assertNotNull(result);
        assertEquals(updatedAddress.getAddressLine1(), result.getAddressLine1());
        assertEquals(updatedAddress.getCity(), result.getCity());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void updateAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            addressService.updateAddress(1L, address);
        });

        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, never()).save(any(Address.class));
    }

    @Test
    void deleteAddress_Success() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        assertDoesNotThrow(() -> {
            addressService.deleteAddress(1L);
        });

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAddress_NotFound() {
        when(addressRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            addressService.deleteAddress(1L);
        });

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, never()).deleteById(any());
    }
} 