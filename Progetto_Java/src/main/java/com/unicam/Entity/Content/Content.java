package com.unicam.Entity.Content;

import com.unicam.Entity.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Content {
    private long id;
    private String title;
    private String description;
    private User author;
    private ContentStatus status;
    private String municipality;


    public Content() {}

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ContentStatus getStatus() {
        return status;
    }

    public void setStatus(ContentStatus status) {
        this.status = status;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }
}
