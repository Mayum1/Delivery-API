package com.deliveryapi.services;

import com.deliveryapi.models.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

}
