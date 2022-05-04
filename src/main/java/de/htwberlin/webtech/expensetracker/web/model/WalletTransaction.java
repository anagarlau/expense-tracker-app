package de.htwberlin.webtech.expensetracker.web.model;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import de.htwberlin.webtech.expensetracker.persistence.entities.WalletEntity;
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
public abstract class WalletTransaction {
    private Long id;
    private Category category;

    private Wallet wallet;

    private LocalDate transactionDate;

    private String transactionDescription;

    private BigDecimal transactionTotal;

    public WalletTransaction(Category category, Wallet wallet, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        this.category = category;
        this.wallet = wallet;
        this.transactionDescription = transactionDescription;
        this.transactionTotal = transactionTotal;
        this.transactionDate = transactionDate;
    }
}
