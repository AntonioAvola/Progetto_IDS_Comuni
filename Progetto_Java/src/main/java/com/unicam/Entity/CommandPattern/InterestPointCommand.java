package com.unicam.Entity.CommandPattern;

import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.BuilderPattern.InterestPointBuilder;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.User;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.Content.InterestPointService;
import java.util.List;

public class InterestPointCommand implements Command{

    private InterestPoint interestPoint;

    private InterestPointService interestPointService;

    private InterestPointBuilder Builder;


    public InterestPointCommand(InterestPointRequest interestPointRequest, User author, InterestPointService interestPointService, GeoPointService geoPointService, ContentStatus status, List<Double> coordinates){
        this.interestPointService = interestPointService;
        this.Builder = new InterestPointBuilder(geoPointService);
        this.Builder.buildAuthor(author);
        this.Builder.buildStatus(status);
        this.Builder.buildDescription(interestPointRequest.getDescription());
        this.Builder.buildMunicipality(author.getMunicipality());
        this.Builder.buildReference(interestPointRequest.getReference(), coordinates);
        this.Builder.buildTitle(interestPointRequest.getTitle());
        this.interestPoint = this.Builder.result();
    }



    @Override
    public void execute() {
        this.interestPointService.addInterestPoint(interestPoint);
    }
}
