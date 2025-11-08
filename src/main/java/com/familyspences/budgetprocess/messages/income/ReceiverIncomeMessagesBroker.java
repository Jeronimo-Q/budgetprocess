package com.familyspences.budgetprocess.messages.income;

import com.familyspences.budgetprocess.domian.income.Income;
import com.familyspences.budgetprocess.service.income.IncomeService;
import com.familyspences.budgetprocess.utils.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverIncomeMessagesBroker {

    private static final Logger log = LoggerFactory.getLogger(ReceiverIncomeMessagesBroker.class);

    private final MapperJsonObject mapper;
    private final IncomeService service;

    public ReceiverIncomeMessagesBroker(MapperJsonObject mapper, IncomeService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @RabbitListener(queues = "${budget.procesar.queue-income-create}")
    public void receiveCreate(String message) {
        mapper.execute(message, Income.class).ifPresentOrElse(
                income -> {
                    try {
                        service.createIncome(income);
                        log.info("Income creado: {}", income);
                    } catch (Exception e) {
                        log.error("Error creando Income: {}", income, e);
                        throw new AmqpRejectAndDontRequeueException("Error creando Income", e);
                    }
                },
                () -> {
                    log.error("JSON inválido (CREATE): {}", message);
                    throw new AmqpRejectAndDontRequeueException("JSON inválido (CREATE)");
                }
        );
    }

    @RabbitListener(queues = "${budget.procesar.queue-income-update}")
    public void receiveUpdate(String message) {
        mapper.execute(message, Income.class).ifPresentOrElse(
                income -> {
                    try {
                        service.updateIncome(income);
                        log.info("Income actualizado: {}", income.getId());
                    } catch (Exception e) {
                        log.error("Error actualizando Income: {}", income, e);
                        throw new AmqpRejectAndDontRequeueException("Error actualizando Income", e);
                    }
                },
                () -> {
                    log.error("JSON inválido (UPDATE): {}", message);
                    throw new AmqpRejectAndDontRequeueException("JSON inválido (UPDATE)");
                }
        );
    }

    @RabbitListener(queues = "${budget.procesar.queue-income-delete}")
    public void receiveDelete(String message) {
        mapper.execute(message, Income.class).ifPresentOrElse(
                income -> {
                    try {
                        service.deleteIncome(income.getId());
                        log.warn("Income eliminado: {}", income.getId());
                    } catch (Exception e) {
                        log.error("Error eliminando Income: {}", income, e);
                        throw new AmqpRejectAndDontRequeueException("Error eliminando Income", e);
                    }
                },
                () -> {
                    log.error("JSON inválido (DELETE): {}", message);
                    throw new AmqpRejectAndDontRequeueException("JSON inválido (DELETE)");
                }
        );
    }
}
