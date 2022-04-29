package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.persistence.ExpenseEntity;
import de.htwberlin.webtech.expensetracker.web.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {
    private ExpenseRepository transactionRepository;

    @Autowired
    public ExpenseService(ExpenseRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<ExpenseEntity> fetchTransactions(){
        return this.transactionRepository.findAll();
    }
}
