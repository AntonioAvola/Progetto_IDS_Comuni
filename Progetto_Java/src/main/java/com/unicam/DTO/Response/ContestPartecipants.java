package com.unicam.DTO.Response;

import java.util.List;

public class ContestPartecipants {

    private long id;
    private List<Partecipants> partecipants;

    public ContestPartecipants(long id, List<Partecipants> partecipants){
        this.id = id;
        this.partecipants = partecipants;
    }

    public long getId() {
        return id;
    }

    public List<Partecipants> getPartecipants() {
        return partecipants;
    }
}
