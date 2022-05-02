package de.htwberlin.webtech.expensetracker.web.model;

import de.htwberlin.webtech.expensetracker.persistence.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {


    private Long tid;

    private Category category;

    private Wallet wallet;

    private String transactionName;

    private LocalDate expenseDate;

    private String description;

    private BigDecimal transactionTotal;

    private Boolean regretted;

}
