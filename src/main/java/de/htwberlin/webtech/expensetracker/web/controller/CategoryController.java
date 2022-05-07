package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.CategoryJSON;
import de.htwberlin.webtech.expensetracker.web.model.CategoryManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/categories")
    public ResponseEntity<Void> addCategory(@RequestBody CategoryManipulationRequest categoryReq) throws URISyntaxException {
        Category category = this.categoryService.createCategory(categoryReq);
        if (category != null) {
            URI uri = new URI("/api/v1/categories/" + category.getCid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/categories/{type}")
    public ResponseEntity<List<CategoryJSON>> fetchById(@PathVariable String type) {
        List<CategoryJSON> category = this.categoryService.fetchCategoryNamesPerTypeAndUser(type);
        if (category != null) return ResponseEntity.ok(category);
        else return ResponseEntity.badRequest().build();
    }

    //TODO: get all transactions per category

}
