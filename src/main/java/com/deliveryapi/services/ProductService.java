package com.deliveryapi.services;

import com.deliveryapi.models.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    void createProduct(Product product);

    ResponseEntity<Void> updateProduct(Long id, Product product);

    ResponseEntity<Void> deleteProduct(Long id);
}
