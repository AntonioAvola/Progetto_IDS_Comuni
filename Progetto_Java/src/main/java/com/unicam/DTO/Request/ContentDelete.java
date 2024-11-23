package com.unicam.DTO.Request;

import java.util.Locale;

public class ContentDelete {

    private String type;
    private String title;
    public ContentDelete(String title, String type) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.type = type.toLowerCase(Locale.ROOT);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
