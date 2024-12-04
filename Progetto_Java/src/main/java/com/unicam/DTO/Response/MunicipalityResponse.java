package com.unicam.DTO.Response;

public class MunicipalityResponse {

    private long id;
    private String nameOfMunicipality;

    public MunicipalityResponse(long id, String nameOfMunicipality) {
        this.id = id;
        this.nameOfMunicipality = nameOfMunicipality;
    }

    public long getId() {
        return id;
    }

    public String getNameOfMunicipality() {
        return nameOfMunicipality;
    }
}
