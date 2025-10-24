package com.familyspences.budgetprocess.domian.budget;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID budgetId;

    @Column(nullable = false)
    private LocalDate period;

    @Column(nullable = false)
    private double budgetAmount;

    @Column(nullable = false)
    private UUID familyId;

    // En lugar de una relación directa con RegisterUser, guardamos solo su ID
    @Column(nullable = false)
    private UUID responsibleId;

    public Budget() {
    }

    public Budget(UUID budgetId, UUID responsibleId, UUID familyId, double budgetAmount, LocalDate period) {
        this.budgetId = budgetId;
        this.responsibleId = responsibleId;
        this.familyId = familyId;
        this.budgetAmount = budgetAmount;
        this.period = period;
    }

    public UUID getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(UUID budgetId) {
        this.budgetId = budgetId;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public UUID getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(UUID responsibleId) {
        this.responsibleId = responsibleId;
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }
}
