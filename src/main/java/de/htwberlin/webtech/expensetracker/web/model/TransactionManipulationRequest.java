package de.htwberlin.webtech.expensetracker.web.model;

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
public class TransactionManipulationRequest {

//    @NotNull(message = "cid must not be null")
//    @Digits(integer=3,fraction=0, message = "Please provide a valid integer")
    private Long cid;



    private String transactionDescription;

    private LocalDate transactionDate;


    private BigDecimal transactionTotal;
}
