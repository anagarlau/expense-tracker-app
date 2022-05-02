package de.htwberlin.webtech.expensetracker.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="wallet")
public class WalletEntity extends BaseEntity{


    //dependency to expense
    @OneToMany(mappedBy ="wallet", targetEntity = ExpenseEntity.class)
    private List<ExpenseEntity> expenses = new ArrayList<>();

    //dependency to income
    @OneToMany( mappedBy ="wallet", targetEntity = IncomeEntity.class)
    private List<IncomeEntity> incomes = new ArrayList<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wid;

    @Column
    private String walletName;

    @Column( columnDefinition="decimal", precision=5, scale=2)
    private BigDecimal balance;
    @Column
    private LocalDate validFrom;

    @Column
    private LocalDate validUntil;

    public WalletEntity(String walletName, BigDecimal balance, LocalDate validFrom, LocalDate validUntil) {
        this.expenses = expenses;
        this.incomes = incomes;
        this.walletName = walletName;
        this.balance = balance;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }
}
