package com.familyspences.budgetprocess.messages.expense;

import com.familyspences.budgetprocess.domian.expense.Expense;
import com.familyspences.budgetprocess.service.expense.ExpenseService;
import com.familyspences.budgetprocess.utils.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceiverExpenseMessagesBroker {
    private final MapperJsonObject mapper;
    private final ExpenseService expenseService;
    private static final Logger log = LoggerFactory.getLogger(ReceiverExpenseMessagesBroker.class);
    private int delay =4000;

    public ReceiverExpenseMessagesBroker(MapperJsonObject mapper, ExpenseService expenseService) {
        this.mapper = mapper;
        this.expenseService = expenseService;
    }


    @RabbitListener(queues = "${budget.procesar.queue-expense-create}")
    public void createExpense(String messageJson) {
        try{
            Thread.sleep(delay);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("El Hilo fue interrumpido mientras se esperaba: ",e);
        }
        try {
            Optional<Expense> expense = mapper.execute(messageJson, Expense.class);
            if (expense.isPresent()) {
                expenseService.save(expense.get());
            } else {
                log.error("Expense not found");
            }
        }catch (Exception e){
            log.error("Error creating expense: ",e);
        }
    }

    @RabbitListener(queues = "${budget.procesar.queue-expense-update}")
    public void updateExpense(String messageJson) {

    }

    @RabbitListener(queues = "${budget.procesar.queue-expense-delete}")
    public void deleteExpense(String messageJson) {

    }


}
