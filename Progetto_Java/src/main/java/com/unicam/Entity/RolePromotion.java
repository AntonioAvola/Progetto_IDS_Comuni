package com.unicam.Entity;

import jakarta.persistence.*;

@Entity
public class RolePromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Role promotion;
    private String municipality;
    private String justification;

    public RolePromotion(){}

    public RolePromotion(User user,
                         Role promotion,
                         String municipality,
                         String justification){
        this.user = user;
        this.promotion = promotion;
        this.municipality = municipality;
        this.justification = justification;
    }

    public User getUser() {
        return user;
    }

    public Role getPromotion() {
        return promotion;
    }

    public String getMunicipality() {
        return municipality;
    }

    public long getId() {
        return id;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}
