package de.htwberlin.webtech.expensetracker.persistence.entities;

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
public class IncomeEntity extends TransactionEntity {


    public IncomeEntity(UserEntity user,CategoryEntity categoryEntity, String transactionDescription, BigDecimal transactionTotal, LocalDate transactionDate) {
        super(user, categoryEntity, transactionDescription, transactionTotal, transactionDate);
    }
}
