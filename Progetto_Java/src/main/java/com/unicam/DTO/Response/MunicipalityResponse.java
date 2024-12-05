package com.unicam.DTO.Response;

public class MunicipalityResponse {

    private long id;
    private String nameOfMunicipality;
    private String description;

    public MunicipalityResponse(long id, String nameOfMunicipality, String description) {
        this.id = id;
        this.nameOfMunicipality = nameOfMunicipality;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getNameOfMunicipality() {
        return nameOfMunicipality;
    }

    public String getDescription() {
        return description;
    }
}
