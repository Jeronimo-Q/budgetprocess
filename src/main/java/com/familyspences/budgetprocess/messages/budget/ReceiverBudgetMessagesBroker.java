package com.familyspences.budgetprocess.messages.budget;

import com.familyspences.budgetprocess.domian.budget.Budget;
import com.familyspences.budgetprocess.service.budget.BudgetService;
import com.familyspences.budgetprocess.utils.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class ReceiverBudgetMessagesBroker {

    private final MapperJsonObject mapper;
    private final BudgetService budgetService;
    private static final Logger log = LoggerFactory.getLogger(ReceiverBudgetMessagesBroker.class);
    private final int delay = 4000;

    public ReceiverBudgetMessagesBroker(MapperJsonObject mapper, BudgetService budgetService) {
        this.mapper = mapper;
        this.budgetService = budgetService;
    }

    @RabbitListener(queues = "q.budget.create")
    public void createBudget(String messageJson) {
        applyDelay();
        try {
            Optional<Budget> budget = mapper.execute(messageJson, Budget.class);
            if (budget.isPresent()) {
                Budget b = budget.get();
                Budget saved = budgetService.createBudget(
                        b.getFamilyId(),
                        b.getResponsibleId(),
                        b.getBudgetAmount(),
                        b.getPeriod()
                );
                log.info("Presupuesto creado exitosamente: {}", saved.getBudgetId());
            } else {
                log.error("No se pudo deserializar el presupuesto del mensaje");
            }
        } catch (IllegalArgumentException e) {
            log.error("Monto inválido al crear presupuesto: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error al crear presupuesto: ", e);
        }
    }

    @RabbitListener(queues = "q.budget.update")
    public void updateBudget(String messageJson) {
        applyDelay();
        try {
            Optional<Budget> budget = mapper.execute(messageJson, Budget.class);
            if (budget.isPresent()) {
                Budget b = budget.get();
                Budget updated = budgetService.updateBudget(
                        b.getBudgetId(),
                        b.getBudgetAmount()
                );
                log.info("Presupuesto actualizado exitosamente: {}", updated.getBudgetId());
            } else {
                log.error("No se pudo deserializar el presupuesto del mensaje");
            }
        } catch (IllegalArgumentException e) {
            log.error("Monto inválido al actualizar presupuesto: {}", e.getMessage());
        } catch (NoSuchElementException e) {
            log.error("Presupuesto no encontrado para actualizar: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error al actualizar presupuesto: ", e);
        }
    }

    @RabbitListener(queues = "q.budget.delete")
    public void deleteBudget(String messageJson) {
        applyDelay();
        try {
            Optional<Budget> budget = mapper.execute(messageJson, Budget.class);
            if (budget.isPresent()) {
                budgetService.deleteBudget(budget.get().getBudgetId());
                log.info("Presupuesto eliminado exitosamente: {}", budget.get().getBudgetId());
            } else {
                log.error("No se pudo deserializar el presupuesto del mensaje");
            }
        } catch (NoSuchElementException e) {
            log.error("No se puede eliminar: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error al eliminar presupuesto: ", e);
        }
    }

    private void applyDelay() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Hilo interrumpido durante espera: ", e);
        }
    }
}