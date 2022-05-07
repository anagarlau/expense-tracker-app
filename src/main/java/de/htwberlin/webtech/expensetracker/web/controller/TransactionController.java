package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import de.htwberlin.webtech.expensetracker.web.model.TransactionManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {
    private final TransactionService transactionService;


    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;

    }

    /*TODO: current balance in response senden*/
    @PostMapping("/{transactionType}")
    public ResponseEntity<Void> postTransaction(@PathVariable String transactionType, @RequestBody TransactionManipulationRequest request) throws URISyntaxException {
        Transaction transaction = null;
        transaction = this.transactionService.createTransaction(transactionType, request);
        if (transaction != null) {
            URI uri = new URI("/api/v1/expenses/" + transaction.getId());
            return ResponseEntity.created(uri).build();
        } else return ResponseEntity.badRequest().build();

    }
    /*TODO: REVISE*/
    @GetMapping("/transactions/{tid}")
    public ResponseEntity<Transaction> fetchExpenseById( @PathVariable Long tid) {
        Transaction transaction = null;
        transaction = this.transactionService.fetchTransactionById(tid);
        return (transaction != null) ? ResponseEntity.ok(transaction) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/transactions/{tid}")
    public ResponseEntity<Transaction> updateExpense( @RequestBody TransactionManipulationRequest request, @PathVariable(value = "tid") Long tid) throws URISyntaxException {
        Transaction updatableTransaction = null;
        updatableTransaction = this.transactionService.update(tid, request);
        return (updatableTransaction != null) ? ResponseEntity.ok(updatableTransaction) : ResponseEntity.notFound().build();
    }

    /*TODO: kuenftig get by category type in category controller*/
//    @GetMapping("/{transactionType}")
//    public ResponseEntity<List<Transaction>> fetchExpenses(@PathVariable String transactionType) {
//        //all/expenses/incomes
//        List<Transaction> transactions = new ArrayList<>();
//
//        transactions = this.transactionService.findAllForLoggedInUser(transactionType);
//
//        return ResponseEntity.status(HttpStatus.OK).body(transactions);
//    }
}
