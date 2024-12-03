package com.unicam.DTO.Response;

import com.unicam.Entity.Time;

public class ContestResponse {

    private long id;
    private String title;
    private String description;
    private String reward;
    private Time duration;

    public ContestResponse (long id,
                            String title,
                            String description,
                            String reward,
                            Time duration){
        this.id = id;
        this.title = title;
        this.description = description;
        this.reward = reward;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReward() {
        return reward;
    }

    public Time getDuration() {
        return duration;
    }
}
