package com.familyspences.budgetprocess.service.expense;

import com.familyspences.budgetprocess.domian.expense.Expense;
import com.familyspences.budgetprocess.messages.expense.ReceiverExpenseMessagesBroker;
import com.familyspences.budgetprocess.repository.expense.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private static final Logger log = LoggerFactory.getLogger(ReceiverExpenseMessagesBroker.class);

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public void save(Expense expense) {
        try {
            if (expense != null) {
                expenseRepository.save(expense);
                log.info("Expense saved with id " + expense.getId());
            }else{
                log.error("Expense null");
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
