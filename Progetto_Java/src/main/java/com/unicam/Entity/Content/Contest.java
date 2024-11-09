package com.unicam.Entity.Content;

import com.unicam.Entity.Time;
import com.unicam.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class Contest extends Content{

    private Time duration;
    private List<String> participants = new ArrayList<>();
    private String winnerName;
    private String reward;

    public Contest(){
        super();
        this.winnerName = "";
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
