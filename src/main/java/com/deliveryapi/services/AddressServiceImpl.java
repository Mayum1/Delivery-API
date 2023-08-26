package com.deliveryapi.services;

import com.deliveryapi.exceptions.ResourceNotFoundException;
import com.deliveryapi.models.Address;
import com.deliveryapi.repositories.AddressRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class AddressServiceImpl implements AddressService {

    private AddressRepository addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + id + " not found"));
    }

    @Override
    public void createAddress(@Valid Address address) {
        addressRepository.save(address);
    }

    @Override
    public ResponseEntity<Void> updateAddress(Long id, @Valid Address address) {
        if (addressRepository.existsById(id)) {
            address.setId(id);
            addressRepository.save(address);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Address with id " + id + " not found");
        }
    }

    @Override
    public ResponseEntity<Void> deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Address with id " + id + " not found");
        }
    }
}
