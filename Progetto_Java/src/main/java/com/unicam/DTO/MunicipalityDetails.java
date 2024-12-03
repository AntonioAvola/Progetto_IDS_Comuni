package com.unicam.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MunicipalityDetails {

    private Map<String, List<Double>> surface = new HashMap<>();
    private Long postCode;
    private String province;
    private String region;
    private String country;
    private Long population;

    public MunicipalityDetails(){}

    public Map<String, List<Double>> getSurface(){
        return surface;
    }
    public void setSurface(List<Double> min, List<Double> max){
        this.surface.put("coordinate minime", min);
        this.surface.put("coordinate massime", max);
    }

    public Long getPostCode() {
        return postCode;
    }
    public void setPostCode(Long postCode){
        this.postCode = postCode;
    }

    public String getProvince() {
        return province;
    }
    public void setProvince(String province){
        this.province = province;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region){
        this.region = region;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country){
        this.country = country;
    }

    public Long getPopulation() {
        return population;
    }
    public void setPopulation(Long population){
        this.population = population;
    }
}
