package de.htwberlin.webtech.expensetracker.web.model;

import java.math.BigDecimal;

public interface IBalance {
    public BigDecimal getExpenseAmount();
    public BigDecimal getIncomeAmount();
    public BigDecimal getBalance();
}
