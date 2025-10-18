package com.familyspences.budgetprocess.repository.balance;

import com.familyspences.budgetprocess.domian.balance.Closings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MonthlyClosingRepository extends JpaRepository<Closings, UUID> {
}