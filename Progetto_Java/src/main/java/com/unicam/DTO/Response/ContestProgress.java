package com.unicam.DTO.Response;

public class ContestProgress {

    private long id;
    private String title;
    private String status;

    public ContestProgress(long id, String title, String status){
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
