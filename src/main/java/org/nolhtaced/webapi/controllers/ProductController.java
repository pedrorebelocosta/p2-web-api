package org.nolhtaced.webapi.controllers;

import org.nolhtaced.core.exceptions.ProductNotFoundException;
import org.nolhtaced.core.models.Product;
import org.nolhtaced.core.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService = new ProductService(null);

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(
                productService.getAll().stream().peek(product -> {
                    if (product.getImagePath() != null) {
                        product.setImagePath("http://localhost:8080/api/images/" + product.getImagePath());
                    }
                }).toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        try {
            Product p = productService.get(productId);

            if (p.getImagePath() != null) {
                p.setImagePath("http://localhost:8080/api/images/" + p.getImagePath());
            }

            return ResponseEntity.ok(p);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
