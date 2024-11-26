package com.unicam.Service.Content;

import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Repository.Content.GeoPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if(point == null){
            throw new NullPointerException("Il punto non esiste");
        }
        return point;
    }

    public boolean checkGeoPointAlreadyExists(String reference, String municipality) {
        return this.repoGeo.existsByNameAndMunicipality(reference, municipality);
    }
}
