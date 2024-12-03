package com.unicam.Entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class RolePromotion {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Role promotion;

    public RolePromotion(){}

    public RolePromotion(User user,
                         Role promotion){
        this.user = user;
        this.promotion = promotion;
    }

    public User getUser() {
        return user;
    }

    public Role getPromotion() {
        return promotion;
    }
}
