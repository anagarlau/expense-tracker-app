package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long id;

    private Long uid;

    private Category category;

    private LocalDate transactionDate;

    private String transactionDescription;

    private BigDecimal transactionTotal;

    public Transaction(Category category, Long uid, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        this.category = category;
        this.uid = uid;
        this.transactionDescription = transactionDescription;
        this.transactionTotal = transactionTotal;
        this.transactionDate = transactionDate;
    }
}
