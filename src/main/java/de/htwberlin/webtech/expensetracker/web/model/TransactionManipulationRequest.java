package de.htwberlin.webtech.expensetracker.web.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionManipulationRequest {

    @NotNull(message = "cid must not be null")
    @Digits(integer=3,fraction=0, message = "Please provide a valid integer")
    private Long cid;


    @NotBlank(message = "Description must not be blank")
    @Size(min=5, message="Description must be at least 5 characters long")
    private String transactionDescription;

    @NotNull(message = "Date must not be blank")
  //  @PastOrPresent(message = "Date must be either in the past or in the present")
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate transactionDate;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.1", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal transactionTotal;
}
