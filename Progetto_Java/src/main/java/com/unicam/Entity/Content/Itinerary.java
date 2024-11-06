package com.unicam.Entity.Content;

import com.unicam.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class Itinerary extends Content{

    private List<InterestPoint> path = new ArrayList<>();
    private List<Long> idUserFavorites = new ArrayList<>();

    public Itinerary(){
        super();
    }

    public List<InterestPoint> getPath() {
        return path;
    }

    public void setPath(List<InterestPoint> path) {
        this.path = path;
    }

    public List<Long> getIdUserFavorites() {
        return idUserFavorites;
    }

    public void setIdUserFavorites(List<Long> idUserFavorites) {
        this.idUserFavorites = idUserFavorites;
    }
}
