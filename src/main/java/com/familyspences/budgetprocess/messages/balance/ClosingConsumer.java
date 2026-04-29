package com.familyspences.budgetprocess.messages.balance;

import com.familyspences.budgetprocess.confi.messages.balance.BalanceQueueConfig;
import com.familyspences.budgetprocess.domian.balance.MonthlyClosing;
import com.familyspences.budgetprocess.service.balance.DuplicateMonthlyClosingException;
import com.familyspences.budgetprocess.service.balance.MonthlyClosingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClosingConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ClosingConsumer.class);
    private final MonthlyClosingService closingService;
    private final ObjectMapper objectMapper;

    public ClosingConsumer(MonthlyClosingService closingService) {
        this.closingService = closingService;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @RabbitListener(queues = BalanceQueueConfig.QUEUE_NAME)
    public void handleMonthlyClosingRequest(Message message) {
        MonthlyClosing closingData = readClosingMessage(message);
        handleMonthlyClosingRequest(closingData);
    }

    public void handleMonthlyClosingRequest(MonthlyClosing closingData) {
        logger.info("Received message: {}", closingData);
        try {
            closingService.processAndSaveClosing(closingData);
        } catch (DuplicateMonthlyClosingException e) {
            logger.error("Monthly closing duplicate data detected. Message will not be requeued: {}", closingData, e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to process message: {}", closingData, e);
            throw e;
        }
    }

    private MonthlyClosing readClosingMessage(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), MonthlyClosing.class);
        } catch (IOException e) {
            logger.error("Monthly closing message could not be deserialized. Message will not be requeued.", e);
            throw new AmqpRejectAndDontRequeueException("Invalid monthly closing message payload.", e);
        }
    }
}
