package com.familyspences.budgetprocess.service.balance;

import com.familyspences.budgetprocess.domian.balance.Closings;
import com.familyspences.budgetprocess.domian.balance.GeneralBalance;
import com.familyspences.budgetprocess.domian.balance.MonthlyClosing;
import com.familyspences.budgetprocess.repository.balance.MonthlyClosingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyClosingServiceTest {

    @Mock
    private MonthlyClosingRepository repository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;

    private MonthlyClosingService service;

    @BeforeEach
    void setUp() {
        service = new MonthlyClosingService(repository);
        ReflectionTestUtils.setField(service, "entityManager", entityManager);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);
    }

    @Test
    void processAndSaveClosingCreatesOneRecordWhenMonthIsOpen() {
        UUID familyId = UUID.randomUUID();
        MonthlyClosing message = new MonthlyClosing(
                familyId,
                new GeneralBalance(new BigDecimal("500"), new BigDecimal("125"), new BigDecimal("375")),
                LocalDate.of(2026, 4, 30)
        );

        when(repository.countByFamilyIdAndClosingDateBetween(
                familyId,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        )).thenReturn(0L);

        service.processAndSaveClosing(message);

        ArgumentCaptor<Closings> captor = ArgumentCaptor.forClass(Closings.class);
        verify(repository).save(captor.capture());
        Closings saved = captor.getValue();
        assertEquals(familyId, saved.getFamilyId());
        assertEquals(LocalDate.of(2026, 4, 30), saved.getClosingDate());
        assertEquals(new BigDecimal("500"), saved.getTotalIncome());
        assertEquals(new BigDecimal("125"), saved.getTotalExpenses());
        assertEquals(new BigDecimal("375"), saved.getBalance());
    }

    @Test
    void processAndSaveClosingIgnoresMessageWhenOneClosingAlreadyExists() {
        UUID familyId = UUID.randomUUID();
        MonthlyClosing message = new MonthlyClosing(
                familyId,
                new GeneralBalance(new BigDecimal("500"), new BigDecimal("125"), new BigDecimal("375")),
                LocalDate.of(2026, 4, 30)
        );

        when(repository.countByFamilyIdAndClosingDateBetween(
                familyId,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        )).thenReturn(1L);

        service.processAndSaveClosing(message);

        verify(repository, never()).save(any());
    }

    @Test
    void processAndSaveClosingRejectsExistingDuplicateData() {
        UUID familyId = UUID.randomUUID();
        MonthlyClosing message = new MonthlyClosing(
                familyId,
                new GeneralBalance(new BigDecimal("500"), new BigDecimal("125"), new BigDecimal("375")),
                LocalDate.of(2026, 4, 30)
        );

        when(repository.countByFamilyIdAndClosingDateBetween(
                familyId,
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        )).thenReturn(2L);

        assertThrows(DuplicateMonthlyClosingException.class, () -> service.processAndSaveClosing(message));
        verify(repository, never()).save(any());
    }
}
