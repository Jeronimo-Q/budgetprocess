package com.familyspences.budgetprocess.service.balance;

import com.familyspences.budgetprocess.domian.balance.Closings;
import com.familyspences.budgetprocess.domian.balance.MonthlyClosing;
import com.familyspences.budgetprocess.repository.balance.MonthlyClosingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;
import java.util.UUID;

@Service
public class MonthlyClosingService {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyClosingService.class);
    private final MonthlyClosingRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public MonthlyClosingService(MonthlyClosingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void processAndSaveClosing(MonthlyClosing data) {
        Objects.requireNonNull(data, "Monthly closing data is required");
        Objects.requireNonNull(data.familyId(), "Monthly closing familyId is required");
        Objects.requireNonNull(data.closingDate(), "Monthly closing date is required");

        YearMonth targetMonth = YearMonth.from(data.closingDate());
        LocalDate startOfMonth = targetMonth.atDay(1);
        LocalDate endOfMonth = targetMonth.atEndOfMonth();

        logger.info("Processing monthly closing for familyId: {} and month: {}", data.familyId(), targetMonth);

        lockMonthlyClosingSlot(data.familyId(), targetMonth);

        long existingClosings = repository.countByFamilyIdAndClosingDateBetween(data.familyId(), startOfMonth, endOfMonth);
        if (existingClosings == 1) {
            logger.warn("Monthly closing already exists for familyId: {} and month: {}. Message ignored.", data.familyId(), targetMonth);
            return;
        }
        if (existingClosings > 1) {
            throw new DuplicateMonthlyClosingException("Existen " + existingClosings + " cierres mensuales para la familia " + data.familyId() + " en " + targetMonth + ".");
        }

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

    private void lockMonthlyClosingSlot(UUID familyId, YearMonth targetMonth) {
        entityManager.createNativeQuery("SELECT pg_advisory_xact_lock(:familyKey, :monthKey)")
                .setParameter("familyKey", familyId.hashCode())
                .setParameter("monthKey", targetMonth.getYear() * 100 + targetMonth.getMonthValue())
                .getSingleResult();
    }
}
