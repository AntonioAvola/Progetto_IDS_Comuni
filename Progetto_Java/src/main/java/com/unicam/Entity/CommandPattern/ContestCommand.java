package com.unicam.Entity.CommandPattern;

import com.unicam.DTO.ContestRequest;
import com.unicam.Entity.BuilderPattern.ContestBuilder;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.User;
import com.unicam.Service.Content.ContestService;

public class ContestCommand implements Command{

    private Contest contest;

    private ContestService contestService;

    private ContestBuilder Builder;

    public ContestCommand(ContestRequest contestRequest, User author) {
        this.Builder = new ContestBuilder();
        this.Builder.buildAuthor(author);
        this.Builder.buildDescription(contestRequest.getDescription());
        this.Builder.buildStatus(ContentStatus.PENDING);
        this.Builder.buildTitle(contestRequest.getTitle());
        this.Builder.buildDuration(contestRequest.getStart(), contestRequest.getEnd());
        this.Builder.buildMunicipality(author.getMunicipality());
        this.Builder.buildReward(contestRequest.getReward());
        this.contest = this.Builder.result();
    }

    @Override
    public void execute() {
        this.contestService.addContest(contest);
    }
}
