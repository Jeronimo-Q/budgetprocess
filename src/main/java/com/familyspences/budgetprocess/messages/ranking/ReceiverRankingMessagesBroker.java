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


    private final RankingService rankingService;
    private static final Logger log = LoggerFactory.getLogger(ReceiverRankingMessagesBroker.class);


    public ReceiverRankingMessagesBroker( RankingService rankingService) {
        this.rankingService = rankingService;
    }


    @RabbitListener(queues = BudgetRankingQueueConfig.RANKING_QUEUE_NAME)
    public void receiveRanking(Ranking ranking) {
        log.info("Mensaje de Ranking recibido para usuario: {} en período: {}", ranking.getUserId(), ranking.getPeriod());
        try {

            if (ranking != null) {
                rankingService.save(ranking);
            } else {
                log.error("Se recibió un mensaje de Ranking nulo o no se pudo deserializar.");
            }
        } catch (Exception e) {
            log.error("Error al procesar mensaje de Ranking: ", e);
        }
    }

}
