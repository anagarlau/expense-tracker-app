package de.htwberlin.webtech.expensetracker.web.model;

import java.math.BigDecimal;

public interface IBalance {
    BigDecimal getExpenseAmount();
    BigDecimal getIncomeAmount();
    BigDecimal getBalance();
}
