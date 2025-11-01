package com.familyspences.budgetprocess.service.budget;

import com.familyspences.budgetprocess.domian.budget.Budget;
import com.familyspences.budgetprocess.repository.budget.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BudgetService {

    private static final Logger log = LoggerFactory.getLogger(BudgetService.class);
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget saveOrUpdateBudget(UUID familyId, UUID responsibleId, double budgetAmount, LocalDate period) {
        log.info("🔍 Verificando presupuesto existente para la familia {} en el periodo {}", familyId, period);

        Budget existing = budgetRepository.findByFamilyIdAndPeriod(familyId, period);

        if (existing != null) {
            log.info("📘 Presupuesto encontrado. Actualizando monto...");
            existing.setBudgetAmount(budgetAmount);
            existing.setResponsibleId(responsibleId);
            return budgetRepository.save(existing);
        } else {
            log.info("🆕 No existe presupuesto previo. Creando nuevo registro...");
            Budget newBudget = new Budget();
            newBudget.setBudgetId(UUID.randomUUID());
            newBudget.setFamilyId(familyId);
            newBudget.setResponsibleId(responsibleId);
            newBudget.setBudgetAmount(budgetAmount);
            newBudget.setPeriod(period);
            return budgetRepository.save(newBudget);
        }
    }

    public Budget getBudgetByFamilyAndPeriod(UUID familyId, LocalDate period) {
        log.info("📖 Buscando presupuesto para familia {} en el periodo {}", familyId, period);
        return budgetRepository.findByFamilyIdAndPeriod(familyId, period);
    }

    public void deleteBudget(UUID budgetId) {
        log.warn("🗑 Eliminando presupuesto con ID {}", budgetId);
        budgetRepository.deleteById(budgetId);
    }
}
