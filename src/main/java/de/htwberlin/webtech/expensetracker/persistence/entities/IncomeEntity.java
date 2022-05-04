package de.htwberlin.webtech.expensetracker.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="income")
public class IncomeEntity extends  WalletTransactionEntity{


    public IncomeEntity(CategoryEntity categoryEntity, WalletEntity walletEntity, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        super(categoryEntity, walletEntity, transactionDescription, transactionTotal, transactionDate);
    }
}
