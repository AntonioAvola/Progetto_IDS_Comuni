package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.Content;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.Time;
import com.unicam.Entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContestBuilder implements Builder{

    private Contest contest = new Contest();


    @Override
    public void buildTitle(String title) {
        this.contest.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        this.contest.setDescription(description);
    }

    @Override
    public void buildAuthor(User author) {
        this.contest.setAuthor(author);
    }

    @Override
    public void buildStatus(ContentStatus status) {
        this.contest.setStatus(status);
    }

    @Override
    public void buildMunicipality(String municipality) {
        this.contest.setMunicipality(municipality);
    }

    public void buildDuration(LocalDateTime start, LocalDateTime end) {
        Time time = new Time(start, end);
        this.contest.setDuration(time);
    }

    public void buildReward(String reward) {
        this.contest.setReward(reward);
    }

    public Contest result(){
        return this.contest;
    }



}
