package de.htwberlin.webtech.expensetracker.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class Transaction {
    private Long id;

    private Long uid;

    private Category category;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate transactionDate;

    private String transactionDescription;

    private BigDecimal transactionTotal;


}
