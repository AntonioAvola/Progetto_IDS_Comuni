package com.unicam.DTO.Response;

import com.unicam.Service.Content.ReviewService;

import java.util.List;

public class ReviewPOI {

    private String namePOI;
    private String reference;
    private List<ReviewResponse> reviews;

    public ReviewPOI(){}

    public String getNamePOI() {
        return namePOI;
    }

    public void setNamePOI(String namePOI) {
        this.namePOI = namePOI;
    }

    public List<ReviewResponse> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
