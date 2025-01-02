package com.unicam.DTO.Response;

public class ReviewResponse extends ReviewPOIResponse{

    private String poi;
    private String reference;
    public ReviewResponse(String title, String description, String author, String poi, String reference) {
        super(title, description, author);
        this.poi = poi;
        this.reference = reference;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
