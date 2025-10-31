package com.familyspences.budgetprocess.confi.messages.ranking;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BudgetRankingQueueConfig {

    public static final String RANKING_EXCHANGE_NAME = "x.ranking.exchange";
    public static final String RANKING_QUEUE_NAME = "q.ranking.calculate";

    public static final String RANKING_ROUTING_KEY = "budget.ranking.create";

    @Bean
    public TopicExchange rankingExchange() {
        return new TopicExchange(RANKING_EXCHANGE_NAME);
    }

    @Bean
    public Queue rankingQueue() {
        return new Queue(RANKING_QUEUE_NAME, true);
    }

    @Bean
    public Binding rankingBinding() {
        return BindingBuilder
                .bind(rankingQueue())
                .to(rankingExchange())
                .with(RANKING_ROUTING_KEY);
    }
}

