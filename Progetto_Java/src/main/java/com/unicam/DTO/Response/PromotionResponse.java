package com.unicam.DTO.Response;

public class PromotionResponse {

    private long promotionID;

    private String username;

    private String previousRole;

    private String newRole;

    public PromotionResponse(long promotionID, String username, String previousRole, String newRole) {
        this.promotionID = promotionID;
        this.username = username;
        this.previousRole = previousRole;
        this.newRole = newRole;
    }

    public long getPromotionID() {
        return promotionID;
    }

    public String getUsername() {
        return username;
    }

    public String getPreviousRole() {
        return previousRole;
    }

    public String getNewRole() {
        return newRole;
    }
}
