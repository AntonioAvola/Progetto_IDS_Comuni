package com.unicam.DTO.Request;

import com.unicam.Entity.Role;
import com.unicam.Entity.User;

import java.util.Locale;

public class SingInDTO {
    private String username;
    private String name;
    private String password;
    private String email;
    private String municipality;
    private boolean animator;
    private boolean municipalityManger;
    private boolean curator;


    public SingInDTO(String username, String name, String password, String email, String municipality, boolean animator, boolean municipalityManger, boolean curator) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.municipality = municipality.toUpperCase(Locale.ROOT);
        this.animator = animator;
        this.municipalityManger = municipalityManger;
        this.curator = curator;
    }


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getMunicipality() {
        return municipality;
    }

    public boolean isAnimator() {
        return animator;
    }

    public boolean isMunicipalityManger() {
        return municipalityManger;
    }

    public boolean isCurator() {
        return curator;
    }


    public User toEntity(){
        User user = new User(getName(), getUsername(), getMunicipality(), getEmail(), getPassword());
        if(isCurator()){
            user.setRole(Role.CURATOR);
        }else if(isAnimator()){
            user.setRole(Role.ANIMATOR);
        } else if (isMunicipalityManger()) {
            user.setRole(Role.MUNICIPALITY_MANAGER);
        }
        else {
            user.setRole(Role.CONTRIBUTOR);
        }
        user.setVisitedMunicipality(getMunicipality());
        return user;
    }
}
