package com.familyspences.budgetprocess.messages.budget;

import com.familyspences.budgetprocess.domian.budget.Budget;
import com.familyspences.budgetprocess.service.budget.BudgetService;
import com.familyspences.budgetprocess.utils.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceiverBudgetMessagesBroker {
    private final MapperJsonObject mapper;
    private final BudgetService budgetService;
    private static final Logger log = LoggerFactory.getLogger(ReceiverBudgetMessagesBroker.class);
    private int delay = 4000;

    public ReceiverBudgetMessagesBroker(MapperJsonObject mapper, BudgetService budgetService) {
        this.mapper = mapper;
        this.budgetService = budgetService;
    }

    @RabbitListener(queues = "q.budget.create")
    public void createBudget(String messageJson) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("El Hilo fue interrumpido mientras se esperaba: ", e);
        }
        try {
            Optional<Budget> budget = mapper.execute(messageJson, Budget.class);
            if (budget.isPresent()) {
                Budget b = budget.get();
                Budget savedBudget = budgetService.saveOrUpdateBudget(
                        b.getFamilyId(),
                        b.getResponsibleId(),
                        b.getBudgetAmount(),
                        b.getPeriod()
                );
                log.info("Budget created successfully: {}", savedBudget.getBudgetId());
            } else {
                log.error("Budget not found in message");
            }
        } catch (Exception e) {
            log.error("Error creating budget: ", e);
        }
    }

    @RabbitListener(queues = "q.budget.update")
    public void updateBudget(String messageJson) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("El Hilo fue interrumpido mientras se esperaba: ", e);
        }
        try {
            Optional<Budget> budget = mapper.execute(messageJson, Budget.class);
            if (budget.isPresent()) {
                Budget b = budget.get();
                Budget updatedBudget = budgetService.saveOrUpdateBudget(
                        b.getFamilyId(),
                        b.getResponsibleId(),
                        b.getBudgetAmount(),
                        b.getPeriod()
                );
                log.info("Budget updated successfully: {}", updatedBudget.getBudgetId());
            } else {
                log.error("Budget not found in message");
            }
        } catch (Exception e) {
            log.error("Error updating budget: ", e);
        }
    }

    @RabbitListener(queues = "q.budget.delete")
    public void deleteBudget(String messageJson) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("El Hilo fue interrumpido mientras se esperaba: ", e);
        }
        try {
            Optional<Budget> budget = mapper.execute(messageJson, Budget.class);
            if (budget.isPresent()) {
                budgetService.deleteBudget(budget.get().getBudgetId());
                log.info("Budget deleted successfully: {}", budget.get().getBudgetId());
            } else {
                log.error("Budget not found in message");
            }
        } catch (Exception e) {
            log.error("Error deleting budget: ", e);
        }
    }
}