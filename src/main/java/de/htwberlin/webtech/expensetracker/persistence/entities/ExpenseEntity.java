package de.htwberlin.webtech.expensetracker.persistence.entities;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;



@NoArgsConstructor

@Getter
@Setter
@Entity
@Table(name="expense")
public class ExpenseEntity extends WalletTransactionEntity {
    public ExpenseEntity(CategoryEntity categoryEntity, WalletEntity walletEntity, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        super(categoryEntity, walletEntity, transactionDescription, transactionTotal, transactionDate);
    }

    //TODO: expense and income extend an abstract class WalletTransactionEntity which in turn extends BaseEntity ✔
    //TODO: revise MySQL Schema ✔





}
