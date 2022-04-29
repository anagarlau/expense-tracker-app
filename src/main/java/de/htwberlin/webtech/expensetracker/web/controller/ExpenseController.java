package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.persistence.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.web.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseController {

    private ExpenseService transactionService;

    @Autowired
    public ExpenseController(ExpenseService transactionService) {
       this.transactionService = transactionService;
    }

    @GetMapping("/api/v1/transactions")
    public ResponseEntity<List<ExpenseEntity>> fetchTransactions(){
        List<ExpenseEntity> transactions = this.transactionService.fetchTransactions();
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
