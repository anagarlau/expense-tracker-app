package de.htwberlin.webtech.expensetracker.web.controller;
import de.htwberlin.webtech.expensetracker.web.model.*;
import de.htwberlin.webtech.expensetracker.web.service.CategoryService;
import de.htwberlin.webtech.expensetracker.web.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private CategoryService categoryService;
    private TransactionService transactionService;

    @Autowired
    public CategoryController(CategoryService categoryService, TransactionService transactionService) {
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }


    @PostMapping("/categories")
    public ResponseEntity<Void> addCategory(@Valid @RequestBody CategoryManipulationRequest categoryReq) throws URISyntaxException {
        Category category = this.categoryService.createCategory(categoryReq);
        if (category != null) {
            URI uri = new URI("/api/v1/categories/" + category.getCid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest().build();
    }



    @GetMapping("/categories/{type}")
    public ResponseEntity<List<CategoryJSON>> fetchCatNamesAndCids(@PathVariable String type) {
        List<CategoryJSON> category = this.categoryService.fetchCategoryNamesPerTypeAndUser(type);
        if (category != null) return ResponseEntity.ok(category);
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/categories/transactions/{cid}")
    public ResponseEntity<List<Transaction>> fetchTransactionsInCategory(@PathVariable Long cid,
                                                                         @RequestParam Optional<String> from,
                                                                         @RequestParam Optional<String> to){
        LocalDate lowerBound = LocalDate.parse(from.orElse(LocalDate.MIN.toString()));
        LocalDate upperBound = LocalDate.parse(to.orElse(LocalDate.now().toString()));
        List<Transaction> transactionsInCid = this.transactionService.fetchAllByCid(cid, lowerBound, upperBound);
        return  ResponseEntity.ok(transactionsInCid);
    }

    @DeleteMapping("/categories/{cid}")
    public ResponseEntity<IBalance> deleteCategoryAndTransactions(@PathVariable  Long cid){
        boolean isDeleted = this.categoryService.deleteCategory(cid);
        if (isDeleted) {
            IBalance balance = this.transactionService.calculateBalance();
            return ResponseEntity.ok().body(balance);
        } else return ResponseEntity.badRequest().build();
    }

}
