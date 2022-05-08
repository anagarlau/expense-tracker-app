package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import de.htwberlin.webtech.expensetracker.web.model.TransactionManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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


    /*Transactions by Category Type in Range*/
    /*TODO: add sorting*/
    /*TODO: Date parsing acc to format*/
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> fetchTransactionsByCatType(@RequestParam(defaultValue = "false") boolean expense,
                                                                        @RequestParam(defaultValue = "false") boolean income,
                                                                        @RequestParam Optional<String> from,
                                                                        @RequestParam Optional<String> to) {
        LocalDate lowerBound = LocalDate.parse(from.orElse(LocalDate.MIN.toString()));
        LocalDate upperBound = LocalDate.parse(to.orElse(LocalDate.now().toString()));
        if (expense == true && income == false)
            return ResponseEntity.ok(this.transactionService.fetchAllByType(CategoryType.EXPENSE, lowerBound, upperBound));
        if (income == true && expense == false)
            return ResponseEntity.ok(this.transactionService.fetchAllByType(CategoryType.INCOME, lowerBound, upperBound));
        return ResponseEntity.ok(this.transactionService.fetchAll(lowerBound, upperBound));
    }



}
