package com.unicam.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class RolePromotion {
    @Id
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Role promotion;
    private String municipality;

    public RolePromotion(){}

    public RolePromotion(User user,
                         Role promotion, String municipality){
        this.user = user;
        this.promotion = promotion;
        this.municipality = municipality;
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
}
