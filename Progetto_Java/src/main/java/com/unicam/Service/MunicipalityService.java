package com.unicam.Service;

import com.unicam.Entity.Municipality;
import com.unicam.Repository.MunicipalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MunicipalityService {

    @Autowired
    private MunicipalityRepository repoMunicipality;

    public void addMunicipality(Municipality municipality){
        this.repoMunicipality.save(municipality);
    }

    public void removeMunicipality(String nameMunicipality){
        //TODO elimina tutti i contenuti inserenti al comune
        Municipality municipality = this.repoMunicipality.findByName(nameMunicipality);
        this.repoMunicipality.delete(municipality);
    }

    public boolean exists(String name){
        return this.repoMunicipality.existsByName(name);
    }
}
