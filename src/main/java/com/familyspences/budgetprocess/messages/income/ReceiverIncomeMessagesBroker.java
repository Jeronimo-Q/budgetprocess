package com.familyspences.budgetprocess.messages.Income;

import com.familyspences.budgetprocess.confi.messages.income.BudgetIncomeQueueConfig;
import com.familyspences.budgetprocess.domian.income.Income;
import com.familyspences.budgetprocess.domian.users.RegisterUser;
import com.familyspences.budgetprocess.service.income.IncomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ReceiverIncomeMessagesBroker {

    private static final Logger log = LoggerFactory.getLogger(ReceiverIncomeMessagesBroker.class);
    private final IncomeService incomeService;

    public ReceiverIncomeMessagesBroker(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @RabbitListener(queues = BudgetIncomeQueueConfig.QUEUE_INCOME_CREATE)
    public void handleIncomeCreate(Map<String, Object> data) {
        log.info("Received Income CREATE event: {}", data);
        try {
            Income income = mapToIncome(data);
            incomeService.saveFromProducer(income);
            log.info("Income saved successfully: {}", income.getId());
        } catch (Exception e) {
            log.error("Error processing Income CREATE event: {}", e.getMessage(), e);
        }
    }

    // OYENTE CLAVE: Maneja el evento de Actualización
    @RabbitListener(queues = BudgetIncomeQueueConfig.QUEUE_INCOME_UPDATE)
    public void handleIncomeUpdate(Map<String, Object> data) {
        log.info("Received Income UPDATE event: {}", data);
        try {
            Income income = mapToIncome(data);
            // Llama al servicio para actualizar el registro en la DB local
            incomeService.updateFromProducer(income);
            log.info("Income updated successfully: {}", income.getId());
        } catch (Exception e) {
            log.error("Error processing Income UPDATE event: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = BudgetIncomeQueueConfig.QUEUE_INCOME_DELETE)
    public void handleIncomeDelete(Map<String, String> data) {
        log.info("Received Income DELETE event: {}", data);
        incomeService.deleteFromProducer(data);
    }

    private Income mapToIncome(Map<String, Object> data) {
        Income income = new Income();

        income.setId(UUID.fromString((String) data.get("id")));
        income.setTitle((String) data.get("title"));
        income.setDescription((String) data.get("description"));
        income.setPeriod((String) data.get("period"));

        // Manejo robusto del campo 'total' (puede venir como Double, Integer, etc.)
        Object totalValue = data.get("total");
        if (totalValue instanceof Number) {
            income.setTotal(((Number) totalValue).doubleValue());
        } else if (totalValue instanceof String) {
            income.setTotal(Double.parseDouble((String) totalValue));
        } else {
            income.setTotal(0.0);
        }

        income.setFamily(UUID.fromString((String) data.get("family")));

        Map<String, String> respMap = (Map<String, String>) data.get("responsible");
        income.setResponsible(new RegisterUser(UUID.fromString(respMap.get("id"))));

        return income;
    }
}