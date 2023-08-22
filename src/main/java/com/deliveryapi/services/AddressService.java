package com.deliveryapi.services;

import com.deliveryapi.models.Address;

import java.util.List;

public interface AddressService {

    List<Address> getAllAddresses();
    Address getAddressById(Long id);
    void createAddress(Address address);
    boolean updateAddress(Long id, Address address);
    boolean deleteAddress(Long id);

}
