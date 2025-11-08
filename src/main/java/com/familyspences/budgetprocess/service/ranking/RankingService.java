package com.familyspences.budgetprocess.service.ranking;

import com.familyspences.budgetprocess.domian.ranking.Ranking;
import com.familyspences.budgetprocess.repository.ranking.RankingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RankingService {
    private final RankingRepository rankingRepository;
    private static final Logger log = LoggerFactory.getLogger(RankingService.class);

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public void save(Ranking ranking) {
        try {
            if (ranking != null) {
                rankingRepository.save(ranking);
                log.info("Ranking guardado para usuario {} en período {}", ranking.getUserId(), ranking.getPeriod());
            } else {
                log.error("Se recibió un ranking nulo para guardar.");
            }
        } catch (Exception e) {
            log.error("Error al guardar el ranking: " + e.getMessage());
        }
    }
}
