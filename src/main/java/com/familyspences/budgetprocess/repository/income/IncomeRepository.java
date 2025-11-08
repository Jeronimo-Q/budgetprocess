package com.familyspences.budgetprocess.repository.income;

import com.familyspences.budgetprocess.domian.income.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {

    List<Income> findByFamilyId(UUID familyId);

    List<Income> findByResponsibleId(UUID responsibleId);

    List<Income> findByPeriod(String period);
}
