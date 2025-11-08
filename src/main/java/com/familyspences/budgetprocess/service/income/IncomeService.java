package com.familyspences.budgetprocess.service.income;

import com.familyspences.budgetprocess.domian.income.Income;
import com.familyspences.budgetprocess.repository.income.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class IncomeService {

    private final IncomeRepository repository;

    public IncomeService(IncomeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Income createIncome(Income income) {
        return repository.save(income);
    }

    @Transactional(readOnly = true)
    public List<Income> getAllIncomes() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Income> getIncomesByFamily(UUID familyId) {
        return repository.findByFamilyId(familyId);
    }

    @Transactional(readOnly = true)
    public List<Income> getIncomesByResponsible(UUID responsibleId) {
        return repository.findByResponsibleId(responsibleId);
    }

    @Transactional(readOnly = true)
    public List<Income> getIncomesByPeriod(String period) {
        return repository.findByPeriod(period);
    }

    @Transactional
    public Income updateIncome(Income income) {
        if (income == null || income.getId() == null) {
            throw new IllegalArgumentException("El id es requerido para actualizar Income.");
        }
        Income current = repository.findById(income.getId())
                .orElseThrow(() -> new IllegalArgumentException("Income no encontrado: " + income.getId()));
        if (income.getTitle() != null)         current.setTitle(income.getTitle());
        if (income.getDescription() != null)   current.setDescription(income.getDescription());
        if (income.getPeriod() != null)        current.setPeriod(income.getPeriod());
        if (income.getTotal() != null)         current.setTotal(income.getTotal());
        if (income.getResponsibleId() != null) current.setResponsibleId(income.getResponsibleId());
        if (income.getFamilyId() != null)      current.setFamilyId(income.getFamilyId());
        return repository.save(current);
    }

    @Transactional
    public void deleteIncome(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El id es requerido para eliminar Income.");
        }
        repository.deleteById(id);
    }
}
