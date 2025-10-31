package com.familyspences.budgetprocess.messages.ranking;

import com.familyspences.budgetprocess.confi.messages.ranking.BudgetRankingQueueConfig;
import com.familyspences.budgetprocess.domian.expense.Expense;
import com.familyspences.budgetprocess.domian.ranking.Ranking;
import com.familyspences.budgetprocess.service.expense.ExpenseService;
import com.familyspences.budgetprocess.service.ranking.RankingService;
import com.familyspences.budgetprocess.utils.gson.MapperJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceiverRankingMessagesBroker {

    private final MapperJsonObject mapper;
    private final RankingService rankingService;
    private static final Logger log = LoggerFactory.getLogger(ReceiverRankingMessagesBroker.class);


    public ReceiverRankingMessagesBroker(MapperJsonObject mapper, RankingService rankingService) {
        this.mapper = mapper;
        this.rankingService = rankingService;
    }


    @RabbitListener(queues = BudgetRankingQueueConfig.RANKING_QUEUE_NAME)
    public void receiveRanking(String messageJson) {
        log.info("Mensaje de Ranking recibido: {}", messageJson);
        try {
            Optional<Ranking> ranking = mapper.execute(messageJson, Ranking.class);

            if (ranking.isPresent()) {
                rankingService.save(ranking.get());
            } else {
                log.error("No se pudo deserializar el mensaje de Ranking.");
            }
        } catch (Exception e) {
            log.error("Error al procesar mensaje de Ranking: ", e);
        }
    }


}
