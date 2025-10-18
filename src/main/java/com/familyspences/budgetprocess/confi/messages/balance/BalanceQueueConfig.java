package com.familyspences.budgetprocess.confi.messages.balance;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BalanceQueueConfig {
    public static final String EXCHANGE_NAME = "x.closing.events";
    public static final String QUEUE_NAME = "q.month.close.process";
    public static final String ROUTING_KEY = "event.month.close.request";

    @Bean
    public TopicExchange closingExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue closingQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(Queue closingQueue, TopicExchange closingExchange) {
        return BindingBuilder.bind(closingQueue).to(closingExchange).with(ROUTING_KEY);
    }
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
