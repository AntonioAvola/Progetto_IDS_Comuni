package com.unicam.DTO.Response;

public class ContestProgress {

    private long id;
    private String municipality;
    private String title;
    private String reward;
    private String status;

    public ContestProgress(long id, String municipality, String title, String reward, String status){
        this.id = id;
        this.municipality = municipality;
        this.title = title;
        this.reward = reward;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getReward() {
        return reward;
    }
}
