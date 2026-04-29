package com.familyspences.budgetprocess.messages.balance;

import com.familyspences.budgetprocess.domian.balance.GeneralBalance;
import com.familyspences.budgetprocess.domian.balance.MonthlyClosing;
import com.familyspences.budgetprocess.service.balance.DuplicateMonthlyClosingException;
import com.familyspences.budgetprocess.service.balance.MonthlyClosingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClosingConsumerTest {

    @Mock
    private MonthlyClosingService closingService;

    @Test
    void handleMonthlyClosingRequestDeserializesJsonMessageFromProducer() {
        ClosingConsumer consumer = new ClosingConsumer(closingService);
        UUID familyId = UUID.randomUUID();
        String json = """
                {
                  "familyId": "%s",
                  "balance": {
                    "totalIncome": 500,
                    "totalExpenses": 125,
                    "balance": 375
                  },
                  "closingDate": "2026-04-30"
                }
                """.formatted(familyId);

        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(StandardCharsets.UTF_8), properties);

        consumer.handleMonthlyClosingRequest(message);

        verify(closingService).processAndSaveClosing(new MonthlyClosing(
                familyId,
                new GeneralBalance(new BigDecimal("500"), new BigDecimal("125"), new BigDecimal("375")),
                LocalDate.of(2026, 4, 30)
        ));
    }

    @Test
    void handleMonthlyClosingRequestRejectsDuplicateDataWithoutRequeue() {
        MonthlyClosing message = closingMessage();
        ClosingConsumer consumer = new ClosingConsumer(closingService);

        doThrow(new DuplicateMonthlyClosingException("duplicado"))
                .when(closingService)
                .processAndSaveClosing(message);

        assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumer.handleMonthlyClosingRequest(message));
    }

    @Test
    void handleMonthlyClosingRequestRethrowsUnexpectedErrorsForRetry() {
        MonthlyClosing message = closingMessage();
        RuntimeException failure = new RuntimeException("transient");
        ClosingConsumer consumer = new ClosingConsumer(closingService);

        doThrow(failure).when(closingService).processAndSaveClosing(message);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> consumer.handleMonthlyClosingRequest(message));
        assertSame(failure, thrown);
        verify(closingService).processAndSaveClosing(message);
    }

    private MonthlyClosing closingMessage() {
        return new MonthlyClosing(
                UUID.randomUUID(),
                new GeneralBalance(new BigDecimal("500"), new BigDecimal("125"), new BigDecimal("375")),
                LocalDate.of(2026, 4, 30)
        );
    }
}
