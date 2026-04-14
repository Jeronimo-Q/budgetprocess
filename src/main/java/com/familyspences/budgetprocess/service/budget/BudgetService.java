package com.familyspences.budgetprocess.service.budget;

import com.familyspences.budgetprocess.domian.budget.Budget;
import com.familyspences.budgetprocess.repository.budget.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BudgetService {

    private static final Logger log = LoggerFactory.getLogger(BudgetService.class);
    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget createBudget(UUID familyId, UUID responsibleId, double budgetAmount, LocalDate period) {
        if (budgetAmount <= 0) {
            throw new IllegalArgumentException("El monto del presupuesto debe ser mayor que cero.");
        }

        log.info("Creando nuevo presupuesto para familia {} en período {}", familyId, period);

        Budget newBudget = new Budget();
        newBudget.setFamilyId(familyId);
        newBudget.setResponsibleId(responsibleId);
        newBudget.setBudgetAmount(budgetAmount);
        newBudget.setPeriod(period);

        return budgetRepository.save(newBudget);
    }

    public Budget updateBudget(UUID budgetId, double newAmount) {
        if (newAmount <= 0) {
            throw new IllegalArgumentException("El nuevo monto debe ser mayor que cero.");
        }

        Budget existing = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró el presupuesto con ID: " + budgetId));

        log.info("Actualizando monto del presupuesto {}", budgetId);
        existing.setBudgetAmount(newAmount);
        return budgetRepository.save(existing);
    }

    public List<Budget> getBudgetsByFamily(UUID familyId) {
        log.info("Consultando presupuestos de la familia {}", familyId);
        return budgetRepository.findByFamilyId(familyId);
    }

    public List<Budget> getBudgetsByFamilyAndPeriod(UUID familyId, LocalDate period) {
        log.info("Consultando presupuestos de familia {} en período {}", familyId, period);
        return budgetRepository.findByFamilyIdAndPeriod(familyId, period);
    }

    public void deleteBudget(UUID budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new NoSuchElementException(
                    "No se puede eliminar: presupuesto con ID " + budgetId + " no existe.");
        }
        log.warn("Eliminando presupuesto con ID {}", budgetId);
        budgetRepository.deleteById(budgetId);
    }
}