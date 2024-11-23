package com.unicam.DTO.Request;

import java.util.Locale;

public class ContentDelete {

    private String type;
    private long id;
    public ContentDelete(String type, long id) {
        this.type = type.toLowerCase(Locale.ROOT);
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }
}
