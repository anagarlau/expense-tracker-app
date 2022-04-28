package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    private List<Transaction> transactions;

    public TransactionController() {
        this.transactions = new ArrayList<>();
        this.transactions.add(new Transaction(1L, "Kitchen"));
        this.transactions.add(new Transaction(2L, "School"));
    }

    @GetMapping("/api/v1/transactions")
    public ResponseEntity<List<Transaction>> fetchTransactions(){
        return ResponseEntity.ok(this.transactions);
    }
}
