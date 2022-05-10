package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryType;
import de.htwberlin.webtech.expensetracker.web.model.*;
import de.htwberlin.webtech.expensetracker.web.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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



    @GetMapping("/balance")
    public ResponseEntity<IBalance> displayBalance(){
       IBalance bigDecimal = transactionService.calculateBalance();
        return ResponseEntity.ok(bigDecimal);
    }


    /*TODO: current balance in response senden*/
    @PostMapping("/{transactionType}")
    public ResponseEntity<IBalance> postTransaction(@PathVariable String transactionType, @Valid  @RequestBody TransactionManipulationRequest request) throws URISyntaxException {
        Transaction transaction = this.transactionService.createTransaction(transactionType, request);
        if (transaction != null) {
            URI uri = new URI("/api/v1/transactions/" + transaction.getId());
            IBalance balance = this.transactionService.calculateBalance();
            return ResponseEntity.created(uri).body(balance);
        } else return ResponseEntity.badRequest().build();

    }

    @GetMapping("/transactions/{tid}")
    public ResponseEntity<Transaction> fetchExpenseById( @PathVariable Long tid) {
        Transaction transaction = this.transactionService.fetchTransactionById(tid);
        return (transaction != null) ? ResponseEntity.ok(transaction) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/transactions/{tid}")
    public ResponseEntity<IBalance> updateExpense(@Valid @RequestBody TransactionManipulationRequest request, @PathVariable(value = "tid") Long tid){
       Transaction updatableTransaction = this.transactionService.update(tid, request);
        return (updatableTransaction != null) ? ResponseEntity.ok(this.transactionService.calculateBalance()) : ResponseEntity.notFound().build();
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
        LocalDate upperBound = LocalDate.parse(to.orElse(LocalDate.MAX.toString()));
        if (expense == true && income == false)
            return ResponseEntity.ok(this.transactionService.fetchAllByType(CategoryType.EXPENSE, lowerBound, upperBound));
        if (income == true && expense == false)
            return ResponseEntity.ok(this.transactionService.fetchAllByType(CategoryType.INCOME, lowerBound, upperBound));
        return ResponseEntity.ok(this.transactionService.fetchAll(lowerBound, upperBound));
    }


    @DeleteMapping("/transactions/{tid}")
    public ResponseEntity<IBalance> deleteTransaction(@PathVariable Long tid){
        boolean isDeleted = this.transactionService.deleteTransaction(tid);
        if (isDeleted) {
            IBalance balance = this.transactionService.calculateBalance();
            return ResponseEntity.ok().body(balance);
        } else return ResponseEntity.badRequest().build();
    }

}
