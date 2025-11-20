package com.familyspences.budgetprocess.domian.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "users")  // mismo nombre de tabla que en el primer proyecto
public class RegisterUser {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    public RegisterUser() {
    }

    public RegisterUser(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
