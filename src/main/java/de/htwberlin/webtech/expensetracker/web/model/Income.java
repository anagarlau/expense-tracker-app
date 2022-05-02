package de.htwberlin.webtech.expensetracker.web.model;

import de.htwberlin.webtech.expensetracker.persistence.entities.WalletEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Income {




    private Long iid;

    private Wallet wallet;

    private BigDecimal incomeTotal;

    private LocalDate incomeDate;


}
