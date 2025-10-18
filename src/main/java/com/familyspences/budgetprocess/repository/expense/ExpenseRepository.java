package com.familyspences.budgetprocess.repository.expense;

import com.familyspences.budgetprocess.domian.expense.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense,UUID> {
}
