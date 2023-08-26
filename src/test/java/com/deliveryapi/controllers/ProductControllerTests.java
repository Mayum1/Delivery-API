package com.deliveryapi.controllers;


import com.deliveryapi.models.Product;
import com.deliveryapi.repositories.ProductRepository;
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
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductRepository productRepository;

    private final Product PRODUCT_1 = new Product(1L, "Smartphone", 599.99);
    private final Product PRODUCT_2 = new Product(2L, "Laptop", 999.50);
    private final Product PRODUCT_3 = new Product(3L, "Headphones", 149.99);

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        products.add(PRODUCT_1);
        products.add(PRODUCT_2);
        products.add(PRODUCT_3);

        when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].price", is(999.50)));
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Smartphone")));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/delivery/products/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product newProduct = new Product(4L, "Test Product", 1234.56);
        String requestBody = mapper.writeValueAsString(newProduct);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/delivery/products")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(productRepository, times(1)).save(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product(1L, "Updated Product", 6543.21);
        String requestBody = mapper.writeValueAsString(updatedProduct);

        when(productRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/delivery/products/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).save(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {
        Product updatedProduct = new Product(1L, "Updated Product", 6543.21);
        String requestBody = mapper.writeValueAsString(updatedProduct);

        when(productRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/delivery/products/1")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).save(ArgumentMatchers.any(Product.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delivery/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() throws Exception {
        when(productRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/delivery/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(1L);
    }
}
