package com.unicam.Entity.Content;

import jakarta.persistence.Entity;

//@Entity
public class Review extends Content {
    private InterestPoint reference;
    public Review(){}
    public InterestPoint getReference() {
        return this.reference;
    }
    public void setReference(InterestPoint reference) {}

    //TODO la classe è relativa ai singoli punti di interesse
    //TODO specificare
}
