package com.unicam.Service.Content;

import com.unicam.DTO.Response.*;
import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ContestRepository;
import com.unicam.Service.UserService;
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
        else{
            Contest contest = this.repoContest.findById(idContent);
            contest.setStatus(status);
            contest.setActivityStatus(ActivityStatus.WAITING);
            this.repoContest.save(contest);
        }
    }

    /*public List<Contest> getByUser(User user) {
        return this.repoContest.findAllByAuthor(user);
    }*/

    public List<ContestResponse> getByUser(User user) {
        List<Contest> contests = this.repoContest.findAllByAuthor(user);
        return convertResponse(contests);
    }

    public List<ContestClosedResponse> getContestNoWinner(String municipality, ActivityStatus finished) {
        List<Contest> contestsMunicipalityClosed = this.repoContest.findByMunicipalityAndActivityStatus(municipality, finished);
        List<Contest> noWinner = new ArrayList<>();
        for(Contest contest : contestsMunicipalityClosed){
            if(contest.getWinnerName().equals("")){
                noWinner.add(contest);
            }
        }
        return convertClosedResponse(noWinner);
    }

    private List<ContestClosedResponse> convertClosedResponse(List<Contest> contests) {
        List<ContestClosedResponse> response = new ArrayList<>();
        for(Contest contest : contests){
            response.add(new ContestClosedResponse(contest.getId(), contest.getTitle(), contest.getDuration().getFinish()));
        }
        return response;
    }

    public List<Partecipants> getPartecipants(long idContest) {
        Contest contest = this.repoContest.findById(idContest);
        List<Partecipants> partecipants = new ArrayList<>();
        List<User> idPartecipants = contest.getParticipants();
        for(User user : idPartecipants){
            partecipants.add(new Partecipants(user.getId(), user.getUsername()));
        }
        return partecipants;
    }

    public boolean assignWinner(long idContest, User winner) {
        Contest contest = this.repoContest.findById(idContest);
        if(!contest.getParticipants().contains(winner))
            return false;
        contest.setWinnerName(winner.getUsername());
        this.repoContest.save(contest);
        return true;
    }

    public List<ContestProgress> getContestProgress(String municipality) {
        List<Contest> list = this.repoContest.findByMunicipalityAndStatus(municipality, ContentStatus.APPROVED);
        List<ContestProgress> progresses = new ArrayList<>();
        for(Contest contest : list){
            if(contest.getActivityStatus().equals(ActivityStatus.WAITING)) {
                progresses.add(new ContestProgress(contest.getId(), contest.getTitle(), "--- Non iniziato ---"));
            }
            else if(contest.getActivityStatus().equals(ActivityStatus.STARTED)){
                progresses.add(new ContestProgress(contest.getId(), contest.getTitle(), "--- In corso ---"));
            }
            else if (contest.getActivityStatus().equals(ActivityStatus.FINISHED) && contest.getWinnerName().equals("")){
                progresses.add(new ContestFinished(contest.getId(), contest.getTitle(), "--- Terminato ---", "--- Vincitore non assegnato ---"));
            }
            else{
                progresses.add(new ContestFinished(contest.getId(), contest.getTitle(), "--- Terminato ---", contest.getWinnerName()));
            }
        }
        return progresses;
    }

    public boolean partecipateContest(long idContest, User partecipant) {
        Contest contest = this.repoContest.findById(idContest);
        if(contest.getParticipants().contains(partecipant))
            return false;
        contest.getParticipants().add(partecipant);
        this.repoContest.save(contest);
        return true;
    }

    public List<ContestResponse> getContestAvailable(String municipality) {
        List<Contest> contests = this.repoContest.findByMunicipalityAndActivityStatus(municipality, ActivityStatus.WAITING);
        return convertResponse(contests);
    }
}
