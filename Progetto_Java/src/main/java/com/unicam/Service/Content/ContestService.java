package com.unicam.Service.Content;

import com.unicam.DTO.Response.*;
import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
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

    public void validateContest(long idContent, ContentStatus status) {
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

    public List<ContestResponse> getByUser(User user) {
        List<Contest> contests = this.repoContest.findAllByAuthor(user);
        return convertResponse(contests);
    }

    public List<ContestClosedResponse> getContestNoWinner(String municipality, ActivityStatus finished) {
        List<Contest> contestsMunicipalityClosed = this.repoContest.findByMunicipalityAndActivityStatusAndWinnerName(municipality, finished, "");
        return convertClosedResponse(contestsMunicipalityClosed);
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

    public void assignWinner(long idContest, User winner) {
        Contest contest = this.repoContest.findById(idContest);
        contest.setWinnerName(winner.getUsername());
        this.repoContest.save(contest);
    }

    public List<ContestProgress> getContestProgress(String municipality) {
        List<Contest> list = this.repoContest.findByMunicipalityAndStatus(municipality, ContentStatus.APPROVED);
        List<ContestProgress> progresses = new ArrayList<>();
        for(Contest contest : list){
            if(contest.getActivityStatus().equals(ActivityStatus.WAITING)) {
                progresses.add(new ContestProgress(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Non iniziato ---"));
            }
            else if(contest.getActivityStatus().equals(ActivityStatus.STARTED)){
                progresses.add(new ContestProgress(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- In corso ---"));
            }
            else if (contest.getActivityStatus().equals(ActivityStatus.FINISHED) && contest.getWinnerName().equals("")){
                progresses.add(new ContestFinished(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Terminato ---", "--- Vincitore non assegnato ---"));
            }
            else{
                progresses.add(new ContestFinished(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Terminato ---", contest.getWinnerName()));
            }
        }
        return progresses;
    }

    public void partecipateContest(long idContest, User partecipant) {
        Contest contest = this.repoContest.findById(idContest);
        contest.getParticipants().add(partecipant);
        this.repoContest.save(contest);
    }

    public List<ContestResponse> getContestAvailable(String municipality) {
        List<Contest> contest = this.repoContest.findByMunicipalityAndActivityStatus(municipality, ActivityStatus.WAITING);
        return convertResponse(contest);
    }

    /*public List<Contest> getContestPartecipated(User user) {
        List <Contest> contest = this.repoContest.findByMunicipality(user.getMunicipality());
        List <Contest> contestResponse = new ArrayList<>();
        for (Contest found : contest) {
            if(found.getParticipants().contains(user)){
                contestResponse.add(found);
            }
        }
        return contestResponse;
    }*/

    public List<ContestProgress> getContestPartecipated(User user) {
        List<Contest> listContestApproved = this.repoContest.findByStatus(ContentStatus.APPROVED);
        List <ContestProgress> contestResponse = new ArrayList<>();
        for (Contest contest : listContestApproved) {
            if(contest.getParticipants().contains(user)){
                if(contest.getActivityStatus().equals(ActivityStatus.WAITING)) {
                    contestResponse.add(new ContestProgress(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Non iniziato ---"));
                }
                else if(contest.getActivityStatus().equals(ActivityStatus.STARTED)){
                    contestResponse.add(new ContestProgress(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- In corso ---"));
                }
                else if(contest.getActivityStatus().equals(ActivityStatus.FINISHED) && contest.getWinnerName().equals("")){
                    contestResponse.add(new ContestFinished(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Terminato ---", "--- Vincitore non assegnato ---"));
                }
                else if(contest.getActivityStatus().equals(ActivityStatus.FINISHED) && contest.getWinnerName().equals(user.getUsername())){
                    contestResponse.add(new ContestFinished(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Terminato ---", "--- Congratulazioni!!!! Hai vinto ---"));
                }
                else {
                    contestResponse.add(new ContestFinished(contest.getId(), contest.getMunicipality(), contest.getTitle(), contest.getReward(), "--- Terminato ---", "--- Non hai vinto ---"));
                }
            }
        }
        return contestResponse;
    }

    public List<ContestResponse> getContestAvailableNoPartecipated(String municipality, User user) {
        List<Contest> contest = this.repoContest.findByMunicipalityAndActivityStatus(municipality, ActivityStatus.WAITING);
        List <Contest> contestResponse = new ArrayList<>();
        for (Contest found : contest) {
            if(!found.getParticipants().contains(user)){
                contestResponse.add(found);
            }
        }
        return convertResponse(contestResponse);
    }
}
