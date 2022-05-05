package de.htwberlin.webtech.expensetracker.web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class Income extends Transaction {
    public Income(Long id, Long uid ,Category cat,LocalDate transactionDate, String transactionDescription, BigDecimal transactionTotal) {
        super(id, uid,cat,transactionDate, transactionDescription, transactionTotal);
    }


}
