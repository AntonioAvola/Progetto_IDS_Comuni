package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.User;
import com.unicam.Service.Content.GeoPointService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InterestPointBuilder implements Builder {

    private InterestPoint interestPoint = new InterestPoint();

    private GeoPointService geoPointService;

    public InterestPointBuilder(GeoPointService geoPointService) {
        this.geoPointService = geoPointService;
    }

    @Override
    public void buildTitle(String title) {
        this.interestPoint.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        this.interestPoint.setDescription(description);
    }

    @Override
    public void buildAuthor(User author) {
        this.interestPoint.setAuthor(author);
    }

    @Override
    public void buildStatus(ContentStatus status) {
        this.interestPoint.setStatus(status);
    }

    @Override
    public void buildMunicipality(String municipality) {
        this.interestPoint.setMunicipality(municipality);
    }

    public void buildReference(String name, List<Double> coordinates){
        GeoPoint reference = new GeoPoint(name, this.interestPoint.getMunicipality(), coordinates.get(0), coordinates.get(1));
        this.geoPointService.addGeoPoint(reference);
        this.interestPoint.setReference(reference);
    }

    public InterestPoint result(){
        return this.interestPoint;
    }
}
