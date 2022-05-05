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
public class ExpenseEntity extends TransactionEntity {


    public ExpenseEntity(UserEntity user, CategoryEntity categoryEntity,  String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        super(user, categoryEntity,  transactionDescription, transactionTotal, transactionDate);
    }

    //TODO: expense and income extend an abstract class TransactionEntity which in turn extends BaseEntity ✔
    //TODO: revise MySQL Schema ✔





}
