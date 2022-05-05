package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Expense;
import de.htwberlin.webtech.expensetracker.web.model.ExpenseManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import de.htwberlin.webtech.expensetracker.web.model.TransactionManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.ExpenseService;
import de.htwberlin.webtech.expensetracker.web.service.IncomeService;
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
    private final ExpenseService expenseService;
    private final IncomeService incomeService;


    @Autowired
    public TransactionController(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    //gets all types of transactions
    @GetMapping("/{transactionType}")
    public ResponseEntity<List<Transaction>> fetchExpenses(@PathVariable String transactionType) {
        List<Transaction> transactions = new ArrayList<>();
        if(transactionType.equals("expenses")){
            transactions = this.expenseService.findAllForLoggedInUser();
        }
        if(transactionType.equals("incomes")){
             transactions = this.incomeService.findAllForLoggedInUser();
        }

//        if(transactionType.equals("all")){
//            transactions = this.expenseService.findAllForLoggedInUser()
//        }

        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

    @PostMapping("/{transactionType}")
    public ResponseEntity<Void> postTransaction(@PathVariable String transactionType, @RequestBody TransactionManipulationRequest request) throws URISyntaxException {
       Transaction transaction = null;
        if(transactionType.equals("expenses")){
            transaction = this.expenseService.createExpense(request);
        }
        if(transactionType.equals("incomes")){
           transaction = this.incomeService.createIncome(request);
        }

        if (transaction != null) {
            URI uri = new URI("/api/v1/expenses/" + transaction.getId());
            return ResponseEntity.created(uri).build();
        }
        else return ResponseEntity.badRequest().build();

    }

    @GetMapping("/{transactionType}/{tid}")
    public ResponseEntity<Transaction> fetchExpenseById(@PathVariable String transactionType, @PathVariable Long tid) {
        Transaction transaction = null;
        if(transactionType.equals("expenses")){
            transaction = this.expenseService.fetchExpenseById(tid);
        }
        if(transactionType.equals("incomes")){
            //add income service
        }
        return (transaction  != null) ? ResponseEntity.ok(transaction ) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{transactionType}/{tid}")
    public ResponseEntity<Transaction> updateExpense(@PathVariable String transactionType, @RequestBody TransactionManipulationRequest expense, @PathVariable(value = "tid") Long tid) throws URISyntaxException {
       Transaction updatableTransaction = null;
        if(transactionType.equals("expenses")){
            updatableTransaction = this.expenseService.update(tid, expense);
        }
        if(transactionType.equals("incomes")){
            //incomeService call
        }
        return (updatableTransaction != null) ? ResponseEntity.ok(updatableTransaction) : ResponseEntity.notFound().build();
    }
}
