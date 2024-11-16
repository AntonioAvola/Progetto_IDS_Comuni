package com.unicam.Entity;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Time {

    private LocalDateTime start;
    private LocalDateTime finish;

    public Time(){}
    public Time(LocalDateTime start,
                LocalDateTime finish){
        this.start = start;
        this.finish = finish;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }
}
