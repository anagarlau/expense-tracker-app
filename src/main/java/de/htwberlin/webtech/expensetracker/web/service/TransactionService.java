package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.persistence.entities.TransactionEntity;
import de.htwberlin.webtech.expensetracker.web.model.Transaction;
import de.htwberlin.webtech.expensetracker.web.model.TransactionManipulationRequest;

import java.util.List;

public interface TransactionService {

    List<Transaction> findAllForLoggedInUser();
    Transaction createTransaction(TransactionManipulationRequest expenseRequest) ;
    Transaction fetchTransactionById(Long tid);
    Transaction update(Long id, TransactionManipulationRequest expenseRequest);



}
