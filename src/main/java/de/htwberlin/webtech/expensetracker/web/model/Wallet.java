package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    private Long wid;
    private String walletName;

    private BigDecimal balance;

    private LocalDate validFrom;

    private LocalDate validUntil;
    private List<Long> expenses= new ArrayList<>();
    private List<Long> incomes= new ArrayList<>();

}
