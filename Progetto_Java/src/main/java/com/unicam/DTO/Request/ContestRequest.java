package com.unicam.DTO.Request;

import java.time.LocalDateTime;
import java.util.Locale;

public class ContestRequest {

    private String title;
    private String description;
    private String reward;
    private LocalDateTime start;
    private LocalDateTime end;

    public ContestRequest(){}

    public ContestRequest(String title, String description, String reward, LocalDateTime start, LocalDateTime end) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        this.reward = reward;
        this.start = start;
        this.end = end;
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

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
