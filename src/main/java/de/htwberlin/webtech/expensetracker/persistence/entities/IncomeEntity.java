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
public class IncomeEntity extends  BaseEntity{
    //dependency to wallet
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="wid", referencedColumnName = "wid", nullable = false)
    private WalletEntity wallet;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iid;

    @Column( columnDefinition="decimal", precision=5, scale=2)
    private BigDecimal incomeTotal;

    @Column
    private LocalDate incomeDate;


    public IncomeEntity(WalletEntity wallet, BigDecimal incomeTotal, LocalDate incomeDate) {
        this.wallet = wallet;
        this.incomeTotal = incomeTotal;
        this.incomeDate=incomeDate;
    }
}
