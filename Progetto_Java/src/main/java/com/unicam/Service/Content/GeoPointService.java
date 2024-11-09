package com.unicam.Service.Content;

import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Repository.Content.GeoPointRepository;

public class GeoPointService {

    private GeoPointRepository repoGeo;

    public void addGeoPoint(GeoPoint point){
        if(this.exists(point))
            throw new UnsupportedOperationException("Il punto esiste già");
        this.repoGeo.save(point);
    }

    public void removeGeoPoint(GeoPoint point){
        if(!this.exists(point))
            throw new UnsupportedOperationException("Il punto non esiste");
        this.repoGeo.delete(point);
    }

    private boolean exists(GeoPoint point){
        if(this.repoGeo.existsByNameAndMunicipality(point.getName(), point.getMunicipality()))
            return true;
        else
            return false;
    }

    public GeoPoint getPoint(String reference, String municipality) {
        GeoPoint point = this.repoGeo.findByNameAndMunicipality(reference, municipality);
        if(point == null){
            throw new NullPointerException("Il punto non esiste");
        }
        return point;
    }
}
