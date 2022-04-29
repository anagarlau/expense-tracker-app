package de.htwberlin.webtech.expensetracker.persistence;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="transaction")
public class ExpenseEntity {
    //add validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;


    @Column
    private String transactionName;

    @Column
    private String description;

    @Column( columnDefinition="decimal", precision=5, scale=2)
    private BigDecimal transactionTotal;


    @Column
    private String regretted;

}
