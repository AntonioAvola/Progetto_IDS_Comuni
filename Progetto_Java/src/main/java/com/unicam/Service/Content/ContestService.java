package com.unicam.Service.Content;

import com.unicam.DTO.Response.ContestResponse;
import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContestService {

    @Autowired
    private ContestRepository repoContest;


    public void addContest(Contest contest){
        this.repoContest.save(contest);
    }

    public void removeContest(long idContest){
        Contest contest = this.repoContest.findById(idContest);
        this.repoContest.delete(contest);
    }


    private boolean exists(Contest contest){
        if(this.repoContest.existsByTitleAndMunicipality(contest.getTitle(), contest.getMunicipality())){
            return true;
        }else{
            return false;
        }
    }

    public boolean getAndRemoveContest(long idContest, User author) {
        if(!this.repoContest.existsByIdAndAuthor(idContest, author))
            return false;
        this.removeContest(idContest);
        return true;
    }

    public void removeContestUser(User user){
        List<Contest> contest = this.repoContest.findAllByAuthor(user);
        this.repoContest.deleteAll(contest);
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

    public List<ContestResponse> getContest(String municipality, ContentStatus pending) {
        List<Contest> contests = this.repoContest.findByMunicipalityAndStatus(municipality, pending);
        return convertResponse(contests);
    }

    private List<ContestResponse> convertResponse(List<Contest> contests) {
        List<ContestResponse> response = new ArrayList<>();
        for(Contest contest : contests){
            ContestResponse convert = new ContestResponse(contest.getId(), contest.getTitle(),
                    contest.getDescription(), contest.getReward(), contest.getDuration());
            response.add(convert);
        }
        return response;
    }

    public boolean checkMunicipality(long idContent, String municipality) {
        return this.repoContest.existsByIdAndMunicipality(idContent, municipality);
    }

    public void approveOrRejectItinerary(long idContent, ContentStatus status) {
        if(status.equals(ContentStatus.REJECTED)) {
            this.removeContest(idContent);
        }
        Contest point = this.repoContest.findById(idContent);
        point.setStatus(status);
        this.repoContest.save(point);
    }
}
