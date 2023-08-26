package com.deliveryapi.controllers;

import com.deliveryapi.models.Address;
import com.deliveryapi.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/delivery/addresses")
@Tag(name = "Контроллер адресов", description = "Контроллер управления адресами доставки")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(
            summary = "Получить все адреса",
            description = "Позволяет получить информацию о всех созданных адресах"
    )
    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();

        return addresses != null && !addresses.isEmpty()
                ? new ResponseEntity<>(addresses, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Получить адрес",
            description = "Позволяет получить информацию об адресе по его id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(
            @PathVariable @Parameter(description = "Идентификатор адреса") Long id
    ) {
        Address address = addressService.getAddressById(id);

        return address != null
                ? new ResponseEntity<>(address, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Создать адрес",
            description = "Создает новый адрес"
    )
    @PostMapping
    public ResponseEntity<?> createAddress(
            @RequestBody @Valid Address address
    ) {
        addressService.createAddress(address);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить адрес",
            description = "Изменяет информацию созданного адреса"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(
            @PathVariable @Parameter(description = "Идентификтор адреса") Long id,
            @RequestBody @Valid Address address
    ) {
        addressService.updateAddress(id, address);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Удалить адрес",
            description = "Удаляет адрес по его id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable @Parameter(description = "Идентификтор адреса") Long id
    ) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok().build();
    }

}
