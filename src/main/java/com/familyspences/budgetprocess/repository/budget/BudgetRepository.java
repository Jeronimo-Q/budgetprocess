package com.familyspences.budgetprocess.repository.budget;

import com.familyspences.budgetprocess.domian.budget.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {

    Budget findByFamilyIdAndPeriod(UUID familyId, LocalDate period);
}
