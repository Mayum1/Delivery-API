package com.deliveryapi.services;

import com.deliveryapi.exceptions.ResourceNotFoundException;
import com.deliveryapi.models.Product;
import com.deliveryapi.repositories.ProductRepository;
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
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product());
        mockProducts.add(new Product());

        when(productRepository.findAll()).thenReturn(mockProducts);

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
    }

    @Test
    void testGetProductsById() {
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        Product product = productService.getProductById(productId);

        assertEquals(productId, product.getId());
    }

    @Test
    void testCreateProduct() {
        Product mockProduct = new Product();

        productService.createProduct(mockProduct);

        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);

        when(productRepository.existsById(productId)).thenReturn(true);

        ResponseEntity<Void> response = productService.updateProduct(productId, mockProduct);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(productRepository, times(1)).save(mockProduct);
    }

    @Test
    void testUpdateProduct_notFound() {
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, mockProduct));

        verify(productRepository, never()).save(mockProduct);
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        ResponseEntity<Void> response = productService.deleteProduct(productId);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProduct_notFound() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId));

        verify(productRepository, never()).deleteById(productId);
    }
}
