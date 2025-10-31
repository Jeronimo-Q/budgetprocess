package com.familyspences.budgetprocess.repository.ranking;

import com.familyspences.budgetprocess.domian.ranking.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, UUID> {
}
