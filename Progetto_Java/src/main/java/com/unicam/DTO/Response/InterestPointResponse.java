package com.unicam.DTO.Response;

public class InterestPointResponse {

    private String title;
    private String description;
    private String reference;

    public InterestPointResponse(String title,
                                 String description,
                                 String reference){
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
