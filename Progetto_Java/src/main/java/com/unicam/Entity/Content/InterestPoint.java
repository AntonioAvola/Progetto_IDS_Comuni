package com.unicam.Entity.Content;

import com.unicam.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class InterestPoint extends Content{

    private GeoPoint reference;
    private List<Long> idUserFavorites = new ArrayList<>();
    //TODO aggiungere i file multimediali

    public InterestPoint(){
        super();
    }

    public GeoPoint getReference() {
        return reference;
    }

    public List<Long> getIdUserFavorites() {
        return idUserFavorites;
    }

    public void setIdUserFavorites(List<Long> idUserFavorites) {
        this.idUserFavorites = idUserFavorites;
    }

    public void setReference(GeoPoint reference) {
        this.reference = reference;
    }
}
