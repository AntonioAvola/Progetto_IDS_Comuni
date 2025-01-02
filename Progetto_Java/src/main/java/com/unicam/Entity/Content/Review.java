package com.unicam.Entity.Content;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
public class Review extends Content {
    @ManyToOne
    private InterestPoint reference;
    @ElementCollection
    @Lob
    private List<Media> medias = new ArrayList<>();

    public Review(){}
    public InterestPoint getReference() {
        return this.reference;
    }
    public void setReference(InterestPoint reference) {
        this.reference = reference;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(Media medias) {
        this.medias.add(medias);
    }
}
