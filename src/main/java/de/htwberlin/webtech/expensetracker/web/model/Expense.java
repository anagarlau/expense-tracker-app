package de.htwberlin.webtech.expensetracker.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;



@Getter
@Setter
@NoArgsConstructor
public class Expense extends Transaction {
    public Expense(Long id, Long uid,Category cat, LocalDate transactionDate, String transactionDescription, BigDecimal transactionTotal) {
        super(id,uid,cat,transactionDate, transactionDescription, transactionTotal);
    }



}
