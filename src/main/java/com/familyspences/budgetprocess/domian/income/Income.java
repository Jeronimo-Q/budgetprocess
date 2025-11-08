package com.familyspences.budgetprocess.domian.income;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(length = 50)
    private String period;

    @Column(nullable = false)
    private Double total;

    @Column(name = "responsible_id", nullable = false)
    private UUID responsibleId;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    public Income() {}

    public Income(UUID id, String title, String description, String period, Double total, UUID responsibleId, UUID familyId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.period = period;
        this.total = total;
        this.responsibleId = responsibleId;
        this.familyId = familyId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public UUID getResponsibleId() { return responsibleId; }
    public void setResponsibleId(UUID responsibleId) { this.responsibleId = responsibleId; }

    public UUID getFamilyId() { return familyId; }
    public void setFamilyId(UUID familyId) { this.familyId = familyId; }

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", period='" + period + '\'' +
                ", total=" + total +
                ", responsibleId=" + responsibleId +
                ", familyId=" + familyId +
                '}';
    }
}
