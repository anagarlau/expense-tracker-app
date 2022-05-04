package de.htwberlin.webtech.expensetracker.web.model;

import de.htwberlin.webtech.expensetracker.persistence.entities.WalletEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class Income extends WalletTransaction{
    public Income(Long id, Category cat, Wallet wallet, LocalDate transactionDate, String transactionDescription, BigDecimal transactionTotal) {
        super(id,cat,wallet, transactionDate, transactionDescription, transactionTotal);
    }


}
