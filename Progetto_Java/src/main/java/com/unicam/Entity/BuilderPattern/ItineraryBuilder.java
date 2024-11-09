package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.User;

import java.util.List;

public class ItineraryBuilder implements Builder{

    private Itinerary itinerary = new Itinerary();

    @Override
    public void buildTitle(String title) {
        this.itinerary.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        this.itinerary.setDescription(description);
    }

    @Override
    public void buildAuthor(User author) {
        this.itinerary.setAuthor(author);
    }

    @Override
    public void buildStatus(ContentStatus status) {
        this.itinerary.setStatus(status);
    }

    @Override
    public void buildMunicipality(String municipality) {
        this.itinerary.setMunicipality(municipality);
    }

    public void buildPath(List<InterestPoint> path){
        this.itinerary.setPath(path);
    }

    public Itinerary result(){
        return this.itinerary;
    }
}
