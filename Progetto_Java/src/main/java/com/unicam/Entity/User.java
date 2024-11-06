package com.unicam.Entity;

public class User {
    private long id;
    private String name;
    private String username;
    private String municipality;
    private String visitedMunicipality;
    private String email;
    private String password;
    protected Role role;

    public User(String name,
                String username,
                String municipality,
                String email,
                String password){
        this.name = name;
        this.username = username;
        this.municipality = municipality;
        this.visitedMunicipality = municipality;
        this.email = email;
        this.password = password;
    }
}
