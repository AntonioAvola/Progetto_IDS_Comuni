package com.unicam.Service.Content;

import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Repository.Content.GeoPointRepository;

public class GeoPointService {

    private GeoPointRepository repoGeo;

    public void AddGeoPoint(GeoPoint point){
        if(this.Exists(point))
            throw new UnsupportedOperationException("Il punto esiste già");
        this.repoGeo.save(point);
    }

    public void RemoveGeoPoint(GeoPoint point){
        if(!this.Exists(point))
            throw new UnsupportedOperationException("Il punto non esiste");
        this.repoGeo.delete(point);
    }

    private boolean Exists(GeoPoint point){
        if(this.repoGeo.findByNameAndMunicipality(point.getName(), point.getMunicipality()))
            return true;
        else
            return false;
    }
}
