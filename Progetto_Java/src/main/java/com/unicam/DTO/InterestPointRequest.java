package com.unicam.DTO;

public class InterestPointRequest {

    private String title;
    private String description;
    private String reference;

    public InterestPointRequest(String title, String description, String reference) {
        this.title = title;
        this.description = description;
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }
}
