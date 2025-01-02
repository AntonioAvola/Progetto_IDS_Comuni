package com.unicam.DTO.Response;

import java.util.ArrayList;
import java.util.List;

public class ReviewResponse {

    private String author;
    private String title;
    private String description;
    private List<String> fileUrl;

    public ReviewResponse(String title,
                          String description,
                          String author){
        this.title = title;
        this.description = description;
        this.author = author;
        this.fileUrl = new ArrayList<>();
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl.add(fileUrl);
    }
}
