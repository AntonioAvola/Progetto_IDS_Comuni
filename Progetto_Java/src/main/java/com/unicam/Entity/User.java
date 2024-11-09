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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getVisitedMunicipality() {
        return visitedMunicipality;
    }

    public void setVisitedMunicipality(String visitedMunicipality) {
        this.visitedMunicipality = visitedMunicipality;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
