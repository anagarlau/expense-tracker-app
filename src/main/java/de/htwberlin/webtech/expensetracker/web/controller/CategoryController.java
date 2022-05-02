package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Category;
import de.htwberlin.webtech.expensetracker.web.model.CategoryManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.model.Expense;
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




    @GetMapping("/categories")
    public ResponseEntity<List<Category>> fetchAllCategories(){
        return ResponseEntity.ok(this.categoryService.fetchAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<Void> addCategory(@RequestBody CategoryManipulationRequest categoryReq) throws URISyntaxException {
        Category category = this.categoryService.createCategory(categoryReq);
        URI uri = new URI("/api/v1/categories/" + category.getCid());
        if (category != null) return ResponseEntity.created(uri).build();
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/categories/{cid}")
    public ResponseEntity<Category> fetchById(@PathVariable Long cid){
        Category category = this.categoryService.fetchById(cid);

        if (category != null) return ResponseEntity.ok(category);
        else return ResponseEntity.badRequest().build();
    }




}
