package de.htwberlin.webtech.expensetracker.web.model;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;



@Getter
@Setter
@NoArgsConstructor
public class Expense extends WalletTransaction {
    public Expense(Long id, Category cat, Wallet wallet, LocalDate transactionDate, String transactionDescription, BigDecimal transactionTotal) {
        super(id,cat,wallet, transactionDate, transactionDescription, transactionTotal);
    }

    //TODO: extend WalletTransactionEntity abstract class



}
