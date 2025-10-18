package com.familyspences.budgetprocess.domian.balance;

import java.time.LocalDate;
import java.util.UUID;

public record MonthlyClosing(
        UUID familyId,
        GeneralBalance balance,
        LocalDate closingDate
) {}
