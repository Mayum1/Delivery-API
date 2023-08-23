package com.deliveryapi.services;

import com.deliveryapi.models.Order;
import com.deliveryapi.models.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    void createProduct(Product product);

    boolean updateProduct(Long id, Product product);

    boolean deleteProduct(Long id);
}
