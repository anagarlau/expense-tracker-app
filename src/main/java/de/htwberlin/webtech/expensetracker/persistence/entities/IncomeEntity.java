package de.htwberlin.webtech.expensetracker.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="income")
public class IncomeEntity extends  WalletTransactionEntity{
    //dependency to wallet


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iid;

}
