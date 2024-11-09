package com.unicam.Service;

import com.unicam.Entity.Municipality;
import com.unicam.Repository.MunicipalityRepository;

public class MunicipalityService {

    private MunicipalityRepository repoMunicipality;

    public void addMunicipality(Municipality municipality){
        if(this.exists(municipality.getName()))
            throw new UnsupportedOperationException("Il comune è già presente");
        this.repoMunicipality.save(municipality);
    }

    public void removeMunicipality(String nameMunicipality){
        if(!this.exists(nameMunicipality))
            throw new UnsupportedOperationException("Il comune non è presente");
        //TODO elimina tutti i contenuti inserenti al comune
        Municipality municipality = this.repoMunicipality.findByName(nameMunicipality);
        this.repoMunicipality.delete(municipality);
    }

    public boolean exists(String name){
        return this.repoMunicipality.existsByName(name);
    }
}
