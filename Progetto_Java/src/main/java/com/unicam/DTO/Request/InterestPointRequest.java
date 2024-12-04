package com.unicam.DTO.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Timer;

public class InterestPointRequest {

    private String title;
    private String description;
    private String reference;

    public InterestPointRequest(String title, String description, String reference) {
        this.title = title.toUpperCase(Locale.ROOT);
        this.description = description;
        this.reference = reference.toUpperCase(Locale.ROOT);
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
