package com.deliveryapi.services;

import com.deliveryapi.exceptions.ResourceNotFoundException;
import com.deliveryapi.models.Address;
import com.deliveryapi.repositories.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AddressServiceTests {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllAddresses() {
        List<Address> mockAddresses = new ArrayList<>();
        mockAddresses.add(new Address());
        mockAddresses.add(new Address());

        when(addressRepository.findAll()).thenReturn(mockAddresses);

        List<Address> addresses = addressService.getAllAddresses();

        assertEquals(2, addresses.size());
    }

    @Test
    void testGetAddressById() {
        Long addressId = 1L;
        Address mockAddress = new Address();
        mockAddress.setId(addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(mockAddress));

        Address address = addressService.getAddressById(addressId);

        assertEquals(addressId, address.getId());
    }

    @Test
    void testCreateAddress() {
        Address mockAddress = new Address();

        addressService.createAddress(mockAddress);

        verify(addressRepository, times(1)).save(mockAddress);
    }

    @Test
    void testUpdateAddress() {
        Long addressId = 1L;
        Address mockAddress = new Address();
        mockAddress.setId(addressId);

        when(addressRepository.existsById(addressId)).thenReturn(true);

        ResponseEntity<Void> response = addressService.updateAddress(addressId, mockAddress);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(addressRepository, times(1)).save(mockAddress);
    }

    @Test
    void testUpdateAddress_notFound() {
        Long addressId = 1L;
        Address mockAddress = new Address();
        mockAddress.setId(addressId);

        when(addressRepository.existsById(addressId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(addressId, mockAddress));

        verify(addressRepository, never()).save(mockAddress);
    }

    @Test
    void testDeleteAddress() {
        Long addressId = 1L;

        when(addressRepository.existsById(addressId)).thenReturn(true);

        ResponseEntity<Void> response = addressService.deleteAddress(addressId);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(addressRepository, times(1)).deleteById(addressId);
    }

    @Test
    void testDeleteAddress_notFound() {
        Long addressId = 1L;

        when(addressRepository.existsById(addressId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress(addressId));

        verify(addressRepository, never()).deleteById(addressId);
    }
}
