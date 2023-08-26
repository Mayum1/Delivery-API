package com.deliveryapi.controllers;


import com.deliveryapi.models.Address;
import com.deliveryapi.repositories.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AddressRepository addressRepository;

    private final Address ADDRESS_1 = new Address(1L, "Sunset Boulevard", "Los Angeles", "900001");
    private final Address ADDRESS_2 = new Address(2L, "Baker Street", "London", "111111");
    private final Address ADDRESS_3 = new Address(3L, "Champs-Élysées", "Paris", "750008");

    @Test
    public void testGetAllAddresses() throws Exception {
        List<Address> addresses = new ArrayList<>();
        addresses.add(ADDRESS_1);
        addresses.add(ADDRESS_2);
        addresses.add(ADDRESS_3);

        when(addressRepository.findAll()).thenReturn(addresses);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/addresses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].city", is("Paris")));
    }

    @Test
    public void testGetAddressById() throws Exception {
        when(addressRepository.findById(ADDRESS_1.getId())).thenReturn(Optional.of(ADDRESS_1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/addresses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.street", is("Sunset Boulevard")));
    }

    @Test
    public void testGetAddressById_NotFound() throws Exception {
        when(addressRepository.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/addresses/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAddress() throws Exception {
        Address newAddress = new Address(4L, "Test Street", "Test City", "123456");
        String requestBody = mapper.writeValueAsString(newAddress);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/delivery/addresses")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(addressRepository, times(1)).save(ArgumentMatchers.any(Address.class));
    }

    @Test
    public void testUpdateAddress() throws Exception {
        Address updatedAddress = new Address(1L, "Updated Street", "Updated City", "654321");
        String requestBody = mapper.writeValueAsString(updatedAddress);

        when(addressRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/delivery/addresses/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).save(ArgumentMatchers.any(Address.class));
    }

    @Test
    public void testUpdateAddress_NotFound() throws Exception {
        Address updatedAddress = new Address(1L, "Updated Street", "Updated City", "654321");
        String requestBody = mapper.writeValueAsString(updatedAddress);

        when(addressRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/delivery/addresses/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, never()).save(ArgumentMatchers.any(Address.class));
    }

    @Test
    public void testDeleteAddress() throws Exception {
        when(addressRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delivery/addresses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAddress_NotFound() throws Exception {
        when(addressRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delivery/addresses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(addressRepository, times(1)).existsById(1L);
        verify(addressRepository, never()).deleteById(1L);
    }
}
