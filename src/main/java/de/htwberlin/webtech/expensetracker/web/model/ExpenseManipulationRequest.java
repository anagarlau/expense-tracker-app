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

public class ExpenseManipulationRequest {


    private String expenseName;

    private Long cid;

    private String description;

    private LocalDate expenseDate;
    private BigDecimal expenseTotal;

    private Boolean regretted;



}
