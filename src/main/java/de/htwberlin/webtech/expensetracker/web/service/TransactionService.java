package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.persistence.repository.ExpenseRepository;
import de.htwberlin.webtech.expensetracker.persistence.repository.IncomeRepository;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private IncomeRepository incomeRepository;


    /*all transaction types*/
    public List<Transaction> gimmeAllTheGoodStuff(){
        List<Transaction> expenses = this.expenseService.findAllForLoggedInUser();
        return expenses;
    }
}
