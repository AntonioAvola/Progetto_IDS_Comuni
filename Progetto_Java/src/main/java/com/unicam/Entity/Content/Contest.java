package com.unicam.Entity.Content;

import com.unicam.Entity.Time;
import com.unicam.Entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contest")
public class Contest extends Content{

    @Embedded
    private Time duration;

    @ManyToMany
    @JoinTable(
            name = "contest_partecipants",
            joinColumns = @JoinColumn(name = "contest_Id"),
            inverseJoinColumns = @JoinColumn(name = "partecipant_Id")
    )
    private List<User> participants = new ArrayList<>();
    private String winnerName;
    private String reward;
    private ActivityStatus activityStatus;

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

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
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

    public ActivityStatus getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(ActivityStatus activityStatus) {
        this.activityStatus = activityStatus;
    }
}
