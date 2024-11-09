package com.unicam.Entity.CommandPattern;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Municipality;
import com.unicam.Entity.User;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.MunicipalityService;

import java.util.ArrayList;
import java.util.List;

public class MunicipalityCommand implements Command{

    private Municipality municipality;
    private GeoPoint reference;
    private GeoPointService serviceGeo;
    private MunicipalityService serviceMunicipality;

    //TODO aggiunta proxyOSM

    public MunicipalityCommand(String nameMunicipality, User author){
        List<Double> coordinates = new ArrayList<>();
        //TODO usare proxyOSM
        this.reference = new GeoPoint(nameMunicipality, nameMunicipality, coordinates.get(0), coordinates.get(1));
        this.municipality = new Municipality(nameMunicipality, reference);
        this.municipality.setStatus(ContentStatus.PENDING);
    }
    @Override
    public void execute() {
        this.serviceGeo.addGeoPoint(this.reference);
        this.serviceMunicipality.addMunicipality(this.municipality);
    }
}
