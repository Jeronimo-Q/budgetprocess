package com.familyspences.budgetprocess.service.balance;

public class DuplicateMonthlyClosingException extends RuntimeException {
    public DuplicateMonthlyClosingException(String message) {
        super(message);
    }
}
