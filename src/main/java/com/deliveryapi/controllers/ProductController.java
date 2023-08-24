package com.deliveryapi.controllers;

import com.deliveryapi.models.Product;
import com.deliveryapi.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/delivery/products")
@Tag(name="Контроллер товаров", description = "Контроллер управления товарами доставки")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Получить все товары",
            description = "Позволяет получить информацию о всех созданных товарах"
    )
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        return products != null && !products.isEmpty()
                ? new ResponseEntity<>(products, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Получить товар",
            description = "Позволяет получить информацию о товаре по его id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable @Parameter(description = "Идентификатор товара") Long id
    ) {
        Product product = productService.getProductById(id);

        return product != null
                ? new ResponseEntity<>(product, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Создать товар",
            description = "Создает новый товар"
    )
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody Product product
    ) {
        productService.createProduct(product);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Обновить товар",
            description = "Изменяет информацию созданного товара"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable @Parameter(description = "Идентификатор адреса") Long id,
            @RequestBody Product product
    ) {
        final boolean updated = productService.updateProduct(id, product);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
            summary = "Удалить товар",
            description = "Удаляет товар по его id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable @Parameter(description = "Идентификатор адреса") Long id
    ) {
        final boolean deleted = productService.deleteProduct(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
