package com.unicam.Service.Content;

import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContestService {

    @Autowired
    private ContestRepository repoContest;


    public void addContest(Contest contest){
        if(this.exists(contest)){
            throw new UnsupportedOperationException("Il contest già esiste");
        }
        this.repoContest.save(contest);
    }

    public void removeContest(Contest contest){
        if(!this.exists(contest)){
            throw new UnsupportedOperationException("Il contest non esiste");
        }
        this.repoContest.delete(contest);
    }


    private boolean exists(Contest contest){
        if(this.repoContest.existsByTitleAndMunicipality(contest.getTitle(), contest.getMunicipality())){
            return true;
        }else{
            return false;
        }
    }

    public boolean getAndRemoveContest(String title, User author) {
        if(!this.repoContest.existsByTitleAndAuthor(title, author))
            return false;
        Contest contest = this.repoContest.findByTitleAndAuthor(title, author);
        this.removeContest(contest);
        return true;
    }

    public void removeContestUser(User user){
        List<Contest> contest = this.repoContest.findAllByAuthor(user);
        this.repoContest.deleteAll(contest);
    }

    public boolean checkTitle(String title, String municipality) {
        return this.repoContest.existsByTitleAndMunicipality(title, municipality);
    }

    public boolean checkDuration(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if(start.isAfter(now) && start.isBefore(end))
            return true;
        return false;
    }

    public void updateActivityStatus(LocalDateTime now) {
        List<Contest> contests = this.repoContest.findByStatus(ContentStatus.APPROVED);
        for(Contest contest : contests){
            if(contest.getActivityStatus().equals(ActivityStatus.WAITING)){
                if(contest.getDuration().getStart().isBefore(now)){
                    contest.setActivityStatus(ActivityStatus.STARTED);
                    this.repoContest.save(contest);
                }
            }
            else if(contest.getActivityStatus().equals(ActivityStatus.STARTED)){
                if(contest.getDuration().getFinish().isBefore(now)){
                    contest.setActivityStatus(ActivityStatus.FINISHED);
                    this.repoContest.save(contest);
                }
            }
        }
    }
}
