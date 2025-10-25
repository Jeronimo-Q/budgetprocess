package com.familyspences.budgetprocess.confi.messages.income;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "budget.procesar")
@PropertySource("classpath:application.properties")
public class BudgetIncomeQueueConfig {

    private String exchangeName;
    private String routingKeyIncomeCreate;
    private String routingKeyIncomeUpdate;
    private String routingKeyIncomeDelete;
    private String queueIncomeCreate;
    private String queueIncomeUpdate;
    private String queueIncomeDelete;

    // Getters y Setters
    public String getExchangeName() { return exchangeName; }
    public void setExchangeName(String exchangeName) { this.exchangeName = exchangeName; }

    public String getRoutingKeyIncomeCreate() { return routingKeyIncomeCreate; }
    public void setRoutingKeyIncomeCreate(String routingKeyIncomeCreate) { this.routingKeyIncomeCreate = routingKeyIncomeCreate; }

    public String getRoutingKeyIncomeUpdate() { return routingKeyIncomeUpdate; }
    public void setRoutingKeyIncomeUpdate(String routingKeyIncomeUpdate) { this.routingKeyIncomeUpdate = routingKeyIncomeUpdate; }

    public String getRoutingKeyIncomeDelete() { return routingKeyIncomeDelete; }
    public void setRoutingKeyIncomeDelete(String routingKeyIncomeDelete) { this.routingKeyIncomeDelete = routingKeyIncomeDelete; }

    public String getQueueIncomeCreate() { return queueIncomeCreate; }
    public void setQueueIncomeCreate(String queueIncomeCreate) { this.queueIncomeCreate = queueIncomeCreate; }

    public String getQueueIncomeUpdate() { return queueIncomeUpdate; }
    public void setQueueIncomeUpdate(String queueIncomeUpdate) { this.queueIncomeUpdate = queueIncomeUpdate; }

    public String getQueueIncomeDelete() { return queueIncomeDelete; }
    public void setQueueIncomeDelete(String queueIncomeDelete) { this.queueIncomeDelete = queueIncomeDelete; }

    // --- Beans ---
    @Bean
    public TopicExchange budgetExchangeIncome() {
        return new TopicExchange(getExchangeName());
    }

    @Bean
    public Queue budgetIncomeCreateQueue() {
        return new Queue(getQueueIncomeCreate(), true);
    }

    @Bean
    public Binding bindingIncomeCreate(Queue budgetIncomeCreateQueue, TopicExchange budgetExchangeIncome) {
        return BindingBuilder.bind(budgetIncomeCreateQueue)
                .to(budgetExchangeIncome)
                .with(getRoutingKeyIncomeCreate());
    }

    @Bean
    public Queue budgetIncomeUpdateQueue() {
        return new Queue(getQueueIncomeUpdate(), true);
    }

    @Bean
    public Binding bindingIncomeUpdate(Queue budgetIncomeUpdateQueue, TopicExchange budgetExchangeIncome) {
        return BindingBuilder.bind(budgetIncomeUpdateQueue)
                .to(budgetExchangeIncome)
                .with(getRoutingKeyIncomeUpdate());
    }

    @Bean
    public Queue budgetIncomeDeleteQueue() {
        return new Queue(getQueueIncomeDelete(), true);
    }

    @Bean
    public Binding bindingIncomeDelete(Queue budgetIncomeDeleteQueue, TopicExchange budgetExchangeIncome) {
        return BindingBuilder.bind(budgetIncomeDeleteQueue)
                .to(budgetExchangeIncome)
                .with(getRoutingKeyIncomeDelete());
    }
}
