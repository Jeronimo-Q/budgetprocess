package com.familyspences.budgetprocess.confi.messages.income;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BudgetIncomeQueueConfig {

    @Value("${budget.procesar.exchange-name:${budget.procesar.exchangeName}}")
    private String exchangeName;

    @Value("${budget.procesar.routing-key-income-create:${budget.procesar.routingKeyIncomeCreate}}")
    private String rkIncomeCreate;

    @Value("${budget.procesar.routing-key-income-update:${budget.procesar.routingKeyIncomeUpdate}}")
    private String rkIncomeUpdate;

    @Value("${budget.procesar.routing-key-income-delete:${budget.procesar.routingKeyIncomeDelete}}")
    private String rkIncomeDelete;

    @Value("${budget.procesar.queue-income-create:${budget.procesar.queueIncomeCreate}}")
    private String qIncomeCreate;

    @Value("${budget.procesar.queue-income-update:${budget.procesar.queueIncomeUpdate}}")
    private String qIncomeUpdate;

    @Value("${budget.procesar.queue-income-delete:${budget.procesar.queueIncomeDelete}}")
    private String qIncomeDelete;

    @Bean(name = "budgetExchangeIncome")
    public TopicExchange budgetExchangeIncome() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean(name = "budgetIncomeCreateQueue")
    public Queue budgetIncomeCreateQueue() {
        return QueueBuilder.durable(qIncomeCreate).build();
    }

    @Bean
    public Binding bindingIncomeCreate(
            @Qualifier("budgetIncomeCreateQueue") Queue q,
            @Qualifier("budgetExchangeIncome") TopicExchange ex) {
        return BindingBuilder.bind(q).to(ex).with(rkIncomeCreate);
    }

    @Bean(name = "budgetIncomeUpdateQueue")
    public Queue budgetIncomeUpdateQueue() {
        return QueueBuilder.durable(qIncomeUpdate).build();
    }

    @Bean
    public Binding bindingIncomeUpdate(
            @Qualifier("budgetIncomeUpdateQueue") Queue q,
            @Qualifier("budgetExchangeIncome") TopicExchange ex) {
        return BindingBuilder.bind(q).to(ex).with(rkIncomeUpdate);
    }

    @Bean(name = "budgetIncomeDeleteQueue")
    public Queue budgetIncomeDeleteQueue() {
        return QueueBuilder.durable(qIncomeDelete).build();
    }

    @Bean
    public Binding bindingIncomeDelete(
            @Qualifier("budgetIncomeDeleteQueue") Queue q,
            @Qualifier("budgetExchangeIncome") TopicExchange ex) {
        return BindingBuilder.bind(q).to(ex).with(rkIncomeDelete);
    }
}
