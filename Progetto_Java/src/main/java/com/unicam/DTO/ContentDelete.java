package com.unicam.DTO;

public class ContentDelete {
    private String title;
    private String type;
    public ContentDelete(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
