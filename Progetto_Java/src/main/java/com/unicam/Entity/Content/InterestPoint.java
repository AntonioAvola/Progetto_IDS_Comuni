package com.unicam.Entity.Content;

import com.unicam.Entity.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interest_point")
public class InterestPoint extends Content{

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "position_Id", nullable = false)
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
