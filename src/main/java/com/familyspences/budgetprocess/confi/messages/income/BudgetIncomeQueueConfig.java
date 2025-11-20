package com.familyspences.budgetprocess.confi.messages.income;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BudgetIncomeQueueConfig {

    public static final String EXCHANGE_NAME = "x.income.exchange";

    public static final String QUEUE_INCOME_CREATE = "q.income.create";
    public static final String QUEUE_INCOME_DELETE = "q.income.delete";
    public static final String QUEUE_INCOME_UPDATE = "q.income.update";

    public static final String ROUTING_KEY_CREATE = "income.create";
    public static final String ROUTING_KEY_DELETE = "income.delete";
    public static final String ROUTING_KEY_UPDATE = "income.update";

    @Bean
    public DirectExchange incomeExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue incomeCreateQueue() {
        return new Queue(QUEUE_INCOME_CREATE, true);
    }

    @Bean
    public Queue incomeDeleteQueue() {
        return new Queue(QUEUE_INCOME_DELETE, true);
    }

    @Bean
    public Queue incomeUpdateQueue() {
        return new Queue(QUEUE_INCOME_UPDATE, true);
    }

    @Bean
    public Binding bindIncomeCreate(Queue incomeCreateQueue, DirectExchange incomeExchange) {
        return BindingBuilder
                .bind(incomeCreateQueue)
                .to(incomeExchange)
                .with(ROUTING_KEY_CREATE);
    }

    @Bean
    public Binding bindIncomeDelete(Queue incomeDeleteQueue, DirectExchange incomeExchange) {
        return BindingBuilder
                .bind(incomeDeleteQueue)
                .to(incomeExchange)
                .with(ROUTING_KEY_DELETE);
    }

    @Bean
    public Binding bindIncomeUpdate(Queue incomeUpdateQueue, DirectExchange incomeExchange) {
        return BindingBuilder
                .bind(incomeUpdateQueue)
                .to(incomeExchange)
                .with(ROUTING_KEY_UPDATE);
    }
}
