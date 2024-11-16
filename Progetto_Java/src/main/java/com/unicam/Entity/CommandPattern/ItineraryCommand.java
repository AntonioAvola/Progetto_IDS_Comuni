package com.unicam.Entity.CommandPattern;

import com.unicam.DTO.Request.ItineraryRequest;
import com.unicam.Entity.BuilderPattern.ItineraryBuilder;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.User;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;

public class ItineraryCommand implements Command{

    private Itinerary itinerary;

    private ItineraryBuilder Builder;

    private InterestPointService interestPointService;

    private ItineraryService itineraryService;

    public ItineraryCommand(ItineraryRequest itineraryRequest, ItineraryService itineraryService, InterestPointService interestPointService, User author, ContentStatus status) {
        this.itineraryService = itineraryService;
        this.interestPointService = interestPointService;
        this.Builder = new ItineraryBuilder();
        this.Builder.buildAuthor(author);
        this.Builder.buildDescription(itineraryRequest.getDescription());
        this.Builder.buildMunicipality(author.getMunicipality());
        this.Builder.buildTitle(itineraryRequest.getTitle());
        this.Builder.buildPath(this.interestPointService.getList(itineraryRequest.getPath(), author.getMunicipality()));
        this.Builder.buildStatus(status);
        this.itinerary = this.Builder.result();
    }

    @Override
    public void execute(){
        this.itineraryService.addItineray(this.itinerary);
    }

}
