package com.unicam.Entity.Content;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

import java.util.List;

//@Entity
public class Review extends Content {
    private InterestPoint reference;
    @ElementCollection
    @Lob
    private List<Media> medias;

    public Review(){}
    public InterestPoint getReference() {
        return this.reference;
    }
    public void setReference(InterestPoint reference) {}

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    //TODO la classe è relativa ai singoli punti di interesse
    //TODO specificare
}
