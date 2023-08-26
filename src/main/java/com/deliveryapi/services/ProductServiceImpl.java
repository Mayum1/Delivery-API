package com.deliveryapi.services;

import com.deliveryapi.exceptions.ResourceNotFoundException;
import com.deliveryapi.models.Product;
import com.deliveryapi.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public void createProduct(@Valid Product product) {
        productRepository.save(product);
    }

    @Override
    public ResponseEntity<Void> updateProduct(Long id, @Valid Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            productRepository.save(product);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
    }
}
