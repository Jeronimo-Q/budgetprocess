package com.familyspences.budgetprocess.confi.messages.expense;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix ="budget.procesar")
@PropertySource("classpath:application.properties")
public class BudgetExpenseQueueConfig {

    private String exchangeName;
    private String routingKeyExpenseCreate;
    private String routingKeyExpenseUpdate;
    private String routingKeyExpenseDelete;
    private String queueExpenseCreate;
    private String queueExpenseUpdate;
    private String queueExpenseDelete;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKeyExpenseCreate() {
        return routingKeyExpenseCreate;
    }

    public void setRoutingKeyExpenseCreate(String routingKeyExpenseCreate) {
        this.routingKeyExpenseCreate = routingKeyExpenseCreate;
    }

    public String getRoutingKeyExpenseUpdate() {
        return routingKeyExpenseUpdate;
    }

    public void setRoutingKeyExpenseUpdate(String routingKeyExpenseUpdate) {
        this.routingKeyExpenseUpdate = routingKeyExpenseUpdate;
    }

    public String getRoutingKeyExpenseDelete() {
        return routingKeyExpenseDelete;
    }

    public void setRoutingKeyExpenseDelete(String routingKeyExpenseDelete) {
        this.routingKeyExpenseDelete = routingKeyExpenseDelete;
    }

    public String getQueueExpenseCreate() {
        return queueExpenseCreate;
    }

    public void setQueueExpenseCreate(String queueExpenseCreate) {
        this.queueExpenseCreate = queueExpenseCreate;
    }

    public String getQueueExpenseUpdate() {
        return queueExpenseUpdate;
    }

    public void setQueueExpenseUpdate(String queueExpenseUpdate) {
        this.queueExpenseUpdate = queueExpenseUpdate;
    }

    public String getQueueExpenseDelete() {
        return queueExpenseDelete;
    }

    public void setQueueExpenseDelete(String queueExpenseDelete) {
        this.queueExpenseDelete = queueExpenseDelete;
    }

    @Bean
    public TopicExchange budgetExchange() {
        return new TopicExchange(getExchangeName());
    }

    @Bean
    public Queue budgetExpenseCreateQueue() {
        return new Queue(getQueueExpenseCreate(), true);
    }

    @Bean
    public Binding bindingCreate(Queue budgetExpenseCreateQueue, TopicExchange budgetExchange) {
        return BindingBuilder.bind(budgetExpenseCreateQueue)
                .to(budgetExchange)
                .with(getRoutingKeyExpenseCreate());
    }

    @Bean
    public Queue budgetExpenseUpdateQueue() {
        return new Queue(getQueueExpenseUpdate(), true);
    }

    @Bean
    public Binding bindingUpdate(Queue budgetExpenseUpdateQueue, TopicExchange budgetExchange) {
        return BindingBuilder.bind(budgetExpenseUpdateQueue)
                .to(budgetExchange)
                .with(getRoutingKeyExpenseUpdate());
    }

    @Bean
    public Queue budgetExpenseDeleteQueue() {
        return new Queue(getQueueExpenseDelete(), true);
    }

    @Bean
    public Binding bindingDelete(Queue budgetExpenseDeleteQueue, TopicExchange budgetExchange) {
        return BindingBuilder.bind(budgetExpenseDeleteQueue)
                .to(budgetExchange)
                .with(getRoutingKeyExpenseDelete());
    }
}