package com.unicam.Service;

import com.unicam.DTO.Response.MunicipalityResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Municipality;
import com.unicam.Repository.MunicipalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MunicipalityService {

    @Autowired
    private MunicipalityRepository repoMunicipality;

    public void addMunicipality(Municipality municipality){
        this.repoMunicipality.save(municipality);
    }

    public boolean exists(String name){
        return this.repoMunicipality.existsByName(name);
    }

    public List<String> getAllMunicipalities() {
        List <Municipality> municipalities = this.repoMunicipality.findAllByStatus(ContentStatus.APPROVED);
        List <String> names = new ArrayList<>();
        for (Municipality municipality : municipalities) {
            names.add(municipality.getName());
        }
        return names;
    }

    public List<MunicipalityResponse> getMunicipalityRequests() {
        List<Municipality> municipalities = this.repoMunicipality.findAllByStatus(ContentStatus.PENDING);
        return convertResponse(municipalities);
    }


    private List<MunicipalityResponse> convertResponse(List<Municipality> municipalities) {
        List<MunicipalityResponse> municipalityResponses = new ArrayList<>();
        for(Municipality municipality : municipalities){
            municipalityResponses.add(new MunicipalityResponse(municipality.getId(), municipality.getName(), municipality.getDescription()));
        }
        return municipalityResponses;
    }

    public void validateMunicipality(long idMunicipality, ContentStatus status) {
        if(status.equals(ContentStatus.REJECTED)){
            this.repoMunicipality.deleteById(idMunicipality);
        }else{
            Municipality municipality = this.repoMunicipality.findById(idMunicipality);
            municipality.setStatus(status);
            this.repoMunicipality.save(municipality);
        }
    }
}
