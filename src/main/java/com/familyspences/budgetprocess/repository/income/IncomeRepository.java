package com.familyspences.budgetprocess.repository.income;

import com.familyspences.budgetprocess.domian.income.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {
}
