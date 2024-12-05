package com.unicam.DTO.Response;

public class Partecipants {

    private long id;
    private String username;

    public Partecipants(long id, String username){
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
