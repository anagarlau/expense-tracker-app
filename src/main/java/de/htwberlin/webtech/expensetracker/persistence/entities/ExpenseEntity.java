package de.htwberlin.webtech.expensetracker.persistence.entities;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="expense")
public class ExpenseEntity extends BaseEntity {
    //table dependencies
    //wallet
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="wid", referencedColumnName = "wid", nullable = false)
    private WalletEntity wallet;

    //category
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="cid", referencedColumnName = "cid", nullable = false)
     private CategoryEntity category;



    //add validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;


    @Column
    private String expenseName;

    @Column
    private String description;

    @Column( columnDefinition="decimal", precision=5, scale=2)
    private BigDecimal expenseTotal;

    @Column
    private LocalDate expenseDate;

    @Column(columnDefinition = "bit", insertable = false)
    private Boolean regretted;


    public ExpenseEntity(WalletEntity wallet, CategoryEntity category, String expenseName, String description, BigDecimal expenseTotal, LocalDate expenseDate, Boolean regretted) {
        this.category = category;
        this.expenseName = expenseName;
        this.description = description;
        this.expenseTotal = expenseTotal;
        this.regretted = regretted;
       this.expenseDate = expenseDate;
       this.wallet = wallet;
    }
}
