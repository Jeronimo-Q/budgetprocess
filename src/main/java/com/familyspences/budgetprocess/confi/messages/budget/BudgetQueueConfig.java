package com.familyspences.budgetprocess.confi.messages.budget;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BudgetQueueConfig {

    // ✅ Nombres consistentes con el resto del sistema
    public static final String EXCHANGE_NAME = "x.budget.events";
    public static final String QUEUE_CREATE = "q.budget.create";
    public static final String QUEUE_UPDATE = "q.budget.update";
    public static final String QUEUE_DELETE = "q.budget.delete";

    public static final String ROUTING_KEY_CREATE = "event.budget.create";
    public static final String ROUTING_KEY_UPDATE = "event.budget.update";
    public static final String ROUTING_KEY_DELETE = "event.budget.delete";

    // ✅ Exchange renombrado para evitar conflicto con BudgetExpenseQueueConfig
    @Bean
    public TopicExchange budgetEventsExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue budgetCreateQueue() {
        return new Queue(QUEUE_CREATE, true);
    }

    @Bean
    public Queue budgetUpdateQueue() {
        return new Queue(QUEUE_UPDATE, true);
    }

    @Bean
    public Queue budgetDeleteQueue() {
        return new Queue(QUEUE_DELETE, true);
    }

    // ✅ Bindings renombrados para evitar conflictos
    @Bean
    public Binding budgetBindingCreate(Queue budgetCreateQueue, TopicExchange budgetEventsExchange) {
        return BindingBuilder.bind(budgetCreateQueue)
                .to(budgetEventsExchange)
                .with(ROUTING_KEY_CREATE);
    }

    @Bean
    public Binding budgetBindingUpdate(Queue budgetUpdateQueue, TopicExchange budgetEventsExchange) {
        return BindingBuilder.bind(budgetUpdateQueue)
                .to(budgetEventsExchange)
                .with(ROUTING_KEY_UPDATE);
    }

    @Bean
    public Binding budgetBindingDelete(Queue budgetDeleteQueue, TopicExchange budgetEventsExchange) {
        return BindingBuilder.bind(budgetDeleteQueue)
                .to(budgetEventsExchange)
                .with(ROUTING_KEY_DELETE);
    }

    @Bean("budgetMessageConverter")
    public MessageConverter budgetJacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }
}