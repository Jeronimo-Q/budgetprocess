package com.familyspences.budgetprocess.service.expense;

import com.familyspences.budgetprocess.domian.expense.Expense;
import com.familyspences.budgetprocess.repository.expense.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);
    private static final String EXPENSENULL ="Expense null";

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void save(Expense expense) {
        try {
            if (expense != null) {
                expenseRepository.save(expense);
                log.info("Expense saved with id: {}", expense.getId());
            }else{
                log.error(EXPENSENULL);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void delete(UUID expense) {
        try {
            if (expense != null) {
                expenseRepository.deleteById(expense);
                log.info("Expense delete succesfully");
            }else{
                log.error(EXPENSENULL);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void update(Expense expense) {
        try {
            if (expense != null) {
                expenseRepository.save(expense);
                log.info("Expense update succesfully");
            }else{
                log.error(EXPENSENULL);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
