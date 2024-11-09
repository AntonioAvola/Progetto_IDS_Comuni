package com.unicam.Entity.CommandPattern;

import com.unicam.DTO.InterestPointRequest;
import com.unicam.Entity.BuilderPattern.InterestPointBuilder;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.User;
import com.unicam.Service.Content.InterestPointService;

import java.util.ArrayList;
import java.util.List;

public class InterestPointCommand implements Command{

    private InterestPoint interestPoint;

    private InterestPointService interestPointService;

    //TODO aggiungi proxyOSM

    private InterestPointBuilder Builder;


    public InterestPointCommand(InterestPointRequest interestPointRequest, User author, ContentStatus status){
        this.Builder = new InterestPointBuilder();
        this.Builder.buildAuthor(author);
        this.Builder.buildStatus(status);
        this.Builder.buildDescription(interestPointRequest.getDescription());
        this.Builder.buildMunicipality(author.getMunicipality());
        //TODO chiamata al proxyOSM
        this.Builder.buildReference(interestPointRequest.getReference(), new ArrayList<Double>());
        this.Builder.buildTitle(interestPointRequest.getTitle());
        this.interestPoint = this.Builder.result();
    }



    @Override
    public void execute() {
        this.interestPointService.AddInterestPoint(interestPoint);
    }
}
