package com.familyspences.budgetprocess.domian.expense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String period;

    // CAMBIO: Ahora usa RegisterUser en lugar de FamilyMemberDomain
    @Column(nullable = false, length = 50)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private String responsible;

    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExpenseCategory category;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private UUID familyId;

    // Enum para categorías
    public enum ExpenseCategory {
        ALIMENTACION("Alimentación"),
        TRANSPORTE("Transporte"),
        SERVICIOS("Servicios Públicos"),
        ENTRETENIMIENTO("Entretenimiento"),
        SALUD("Salud y Medicina"),
        EDUCACION("Educación"),
        ROPA("Ropa y Calzado"),
        HOGAR("Hogar y Mantenimiento"),
        OTROS("Otros");

        private final String displayName;

        ExpenseCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructor vacío (requerido por JPA)
    public Expense() {
    }

    // Constructor completo para datos existentes
    public Expense(UUID id, String title, String description, String period,
                   String responsible, BigDecimal value, ExpenseCategory category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.period = period;
        this.responsible = responsible;
        this.value = value;
        this.category = category;
    }

    // Constructor para nuevos gastos (sin ID)
    public Expense(String title, String description, String period,
                   String responsible, BigDecimal value, ExpenseCategory category, UUID familyId) {
        this.title = title;
        this.description = description;
        this.period = period;
        this.responsible = responsible;
        this.value = value;
        this.category = category;
        this.familyId = familyId;
    }



    // Constructor mínimo para gastos básicos
    public Expense(String title, String period, String responsible,
                   BigDecimal value, ExpenseCategory category) {
        this.title = title;
        this.period = period;
        this.responsible = responsible;
        this.value = value;
        this.category = category;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isExpensive() {
        return value != null && value.compareTo(new BigDecimal("1000.00")) > 0;
    }

    public String getFormattedValue() {
        return value != null ? String.format("$%.2f", value) : "$0.00";
    }

    public boolean isSamePeriod(String otherPeriod) {
        return period != null && period.equalsIgnoreCase(otherPeriod);
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getFamilyId() {
        return familyId;
    }

    public void setFamilyId(UUID familyId) {
        this.familyId = familyId;
    }

    public boolean isValidPeriod() {
        if (period == null || period.isBlank()) {
            return false;
        }

        // Verificar formato YYYY-MM
        if (period.matches("\\d{4}-\\d{2}")) {
            String[] parts = period.split("-");
            int month = Integer.parseInt(parts[1]);
            return month >= 1 && month <= 12;
        }

        String[] validMonths = {"enero", "febrero", "marzo", "abril", "mayo", "junio",
                "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

        for (String validMonth : validMonths) {
            if (validMonth.equalsIgnoreCase(period)) {
                return true;
            }
        }

        return false;
    }

    // Método equals mejorado
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense)) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
