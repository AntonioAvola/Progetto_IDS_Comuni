package com.unicam.Service.Content;

import com.unicam.Entity.Content.Contest;
import com.unicam.Repository.Content.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
}
