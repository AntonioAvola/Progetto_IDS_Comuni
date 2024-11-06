package com.unicam.Service.Content;

import com.unicam.Entity.Content.Contest;
import com.unicam.Repository.Content.ContestRepository;

public class ContestService {

    private ContestRepository repoContest;


    public void addContest(Contest contest){
        if(this.Exists(contest)){
            throw new UnsupportedOperationException("Il contest già esiste");
        }
        this.repoContest.save(contest);
    }

    public void removeContest(Contest contest){
        if(!this.Exists(contest)){
            throw new UnsupportedOperationException("Il contest non esiste");
        }
        this.repoContest.delete(contest);
    }


    private boolean Exists(Contest contest){
        if(this.repoContest.findByTitleAndMunicipality(contest.getTitle(), contest.getMunicipality())){
            return true;
        }else{
            return false;
        }
    }
}
