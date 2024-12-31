package com.unicam.Entity.Content;

import jakarta.persistence.*;

@Entity
@Table(name = "media_file")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private byte[] file;
    private String type;

    public Media(){}

    public Media(String name,
                 byte[] file,
                 String type){
        this.name = name;
        this.file = file;
        this.type = type;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
