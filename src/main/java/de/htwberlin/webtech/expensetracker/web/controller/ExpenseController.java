package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.Expense;
import de.htwberlin.webtech.expensetracker.web.model.ExpenseManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> fetchExpenses() {
        List<Expense> transactions = this.expenseService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

    @PostMapping("/expenses")
    public ResponseEntity<Void> postExpense(@RequestBody ExpenseManipulationRequest expenseReq) throws URISyntaxException {
        Expense expense = this.expenseService.createExpense(expenseReq);
        URI uri = new URI("/api/v1/expenses/" + expense.getTid());
        if (expense != null) return ResponseEntity.created(uri).build();
        else return ResponseEntity.badRequest().build();

    }

    @GetMapping("/expenses/{tid}")
    public ResponseEntity<Expense> fetchExpenseById(@PathVariable Long tid) {
        Expense expense = this.expenseService.fetchExpenseById(tid);
        return (expense != null) ? ResponseEntity.ok(expense) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/expenses/{tid}")
    public ResponseEntity<Expense> updateExpense(@RequestBody ExpenseManipulationRequest expense, @PathVariable(value = "tid") Long tid) throws URISyntaxException {
        Expense updatableExpense = this.expenseService.update(tid, expense);
        return (updatableExpense != null) ? ResponseEntity.ok(updatableExpense) : ResponseEntity.notFound().build();
    }
}
