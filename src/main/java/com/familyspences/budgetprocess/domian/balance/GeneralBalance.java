package com.familyspences.budgetprocess.domian.balance;

import java.math.BigDecimal;

public record GeneralBalance(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance
) {}