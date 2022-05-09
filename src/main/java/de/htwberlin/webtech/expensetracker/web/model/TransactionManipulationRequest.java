package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TransactionManipulationRequest {



    @NotNull(message = "cid must not be null")
    @Digits(integer=3,fraction=0, message = "Please provide a valid integer")
    private Long cid;

    @NotBlank
    @Size(min=5, message="Description must be at least 5 characters long")
    private String transactionDescription;

    @NotBlank(message = "Date must not be blank")
    @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message="Please provide a valid date")
    private String transactionDate;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.1", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal transactionTotal;
}
