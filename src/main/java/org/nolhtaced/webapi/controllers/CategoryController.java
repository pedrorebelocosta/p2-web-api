package org.nolhtaced.webapi.controllers;

import org.nolhtaced.core.exceptions.CategoryNotFoundException;
import org.nolhtaced.core.models.Category;
import org.nolhtaced.core.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService = new CategoryService(null);
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }


    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Integer categoryId) {
        try {
            Category c = categoryService.get(categoryId);
            return ResponseEntity.ok(c);
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
