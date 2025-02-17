package com.unicam.Service.Content;

import com.unicam.DTO.Response.GeoPointResponse;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Repository.Content.GeoPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoPointService {

    private final GeoPointRepository repoGeo;

    public GeoPointService(GeoPointRepository repoGeo) {
        this.repoGeo = repoGeo;
    }

    public void addGeoPoint(GeoPoint point){
        this.repoGeo.save(point);
    }

    public void removeGeoPoint(GeoPoint point){
        this.repoGeo.delete(point);
    }

    public GeoPoint getPoint(String reference, String municipality) {
        GeoPoint point = this.repoGeo.findByNameAndMunicipality(reference, municipality);
        return point;
    }

    public GeoPoint getById(long id) {
        return this.repoGeo.findById(id);
    }

}
