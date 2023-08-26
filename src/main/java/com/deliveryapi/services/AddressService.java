package com.deliveryapi.services;

import com.deliveryapi.models.Address;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    List<Address> getAllAddresses();

    Address getAddressById(Long id);

    void createAddress(Address address);

    ResponseEntity<Void> updateAddress(Long id, Address address);

    ResponseEntity<Void> deleteAddress(Long id);

}
