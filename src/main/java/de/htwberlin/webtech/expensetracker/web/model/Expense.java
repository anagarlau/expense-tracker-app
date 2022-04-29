package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
public class Expense {

    private long tid;



    private String transactionName;


    private String description;


    private BigDecimal transactionTotal;



    private String regretted;

}
