package com.familyspences.budgetprocess.service.income;

import com.familyspences.budgetprocess.domian.income.Income;
import com.familyspences.budgetprocess.repository.income.IncomeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class IncomeService {

    private static final Logger log = LoggerFactory.getLogger(IncomeService.class);

    private final IncomeRepository repository;

    public IncomeService(IncomeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveFromProducer(Income income) {
        log.info("Saving income from producer: {}", income);
        repository.save(income);
    }

    // MÉTODO CLAVE: Actualiza el ingreso en la DB del procesador (save funciona como update si el ID existe)
    @Transactional
    public void updateFromProducer(Income income) {
        log.info("Updating income from producer: {}", income);
        // JPA 'save' actualizará el registro existente si el ID ya existe en la base de datos
        repository.save(income);
    }

    @Transactional
    public void deleteFromProducer(Map<String, String> data) {
        try {
            String incomeIdStr = data.get("incomeId");

            if (incomeIdStr == null) {
                log.warn("Missing incomeId in DELETE event: {}", data);
                return;
            }

            UUID incomeId = UUID.fromString(incomeIdStr);

            repository.deleteById(incomeId);

            log.info("Income DELETE event successfully processed for ID: {}", incomeId);

        } catch (Exception e) {
            log.error("Error deleting income from producer event: {}", e.getMessage(), e);
        }
    }
}