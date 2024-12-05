package com.unicam.DTO.Response;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class ContestClosedResponse {

    private long id;
    private String name;
    private LocalDateTime end;

    public ContestClosedResponse(long id,
                                 String name,
                                 LocalDateTime end){
        this.id = id;
        this.name = name;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
