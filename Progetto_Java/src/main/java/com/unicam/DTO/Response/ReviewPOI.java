package com.unicam.DTO.Response;

import java.util.List;

public class ReviewPOI {

    private String namePOI;
    private String reference;
    private List<ReviewPOIResponse> reviews;

    public ReviewPOI(){}

    public String getNamePOI() {
        return namePOI;
    }

    public void setNamePOI(String namePOI) {
        this.namePOI = namePOI;
    }

    public List<ReviewPOIResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewPOIResponse> reviews) {
        this.reviews = reviews;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
