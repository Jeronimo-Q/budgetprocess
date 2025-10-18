package com.familyspences.budgetprocess.service.balance;

import com.familyspences.budgetprocess.domian.balance.Closings;
import com.familyspences.budgetprocess.domian.balance.MonthlyClosing;
import com.familyspences.budgetprocess.repository.balance.MonthlyClosingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonthlyClosingService {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyClosingService.class);
    private final MonthlyClosingRepository repository;

    public MonthlyClosingService(MonthlyClosingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void processAndSaveClosing(MonthlyClosing data) {
        logger.info("Processing monthly closing for familyId: {}", data.familyId());

        Closings record = new Closings(
                data.familyId(),
                data.closingDate(),
                data.balance().totalIncome(),
                data.balance().totalExpenses(),
                data.balance().balance()
        );

        repository.save(record);
        logger.info("Successfully saved monthly closing record with ID: {}", record.getId());
    }
}
