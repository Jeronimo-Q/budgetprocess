package com.familyspences.budgetprocess.messages.balance;

import com.familyspences.budgetprocess.confi.messages.balance.BalanceQueueConfig;
import com.familyspences.budgetprocess.domian.balance.MonthlyClosing;
import com.familyspences.budgetprocess.service.balance.MonthlyClosingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClosingConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ClosingConsumer.class);
    private final MonthlyClosingService closingService;

    public ClosingConsumer(MonthlyClosingService closingService) {
        this.closingService = closingService;
    }

    @RabbitListener(queues = BalanceQueueConfig.QUEUE_NAME)
    public void handleMonthlyClosingRequest(MonthlyClosing closingData) {
        logger.info("Received message: {}", closingData);
        try {
            closingService.processAndSaveClosing(closingData);
        } catch (Exception e) {
            logger.error("Failed to process message: {}", closingData, e);
        }
    }
}