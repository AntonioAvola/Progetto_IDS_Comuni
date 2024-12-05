package com.unicam.Entity.CommandPattern;

import com.unicam.DTO.MunicipalityDetails;
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
    private MunicipalityService serviceMunicipality;

    public MunicipalityCommand(MunicipalityService serviceMunicipality, String nameMunicipality, String description, MunicipalityDetails detalis){
        this.serviceMunicipality = serviceMunicipality;
        this.municipality = new Municipality(nameMunicipality, description, detalis);
    }
    @Override
    public void execute() {
        this.serviceMunicipality.addMunicipality(this.municipality);
    }
}
