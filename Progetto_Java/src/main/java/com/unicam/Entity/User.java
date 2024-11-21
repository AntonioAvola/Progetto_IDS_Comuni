package com.unicam.Entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_platform")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_platform",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_platform"
    )
    private long id;
    private String name;
    private String username;
    private String municipality;
    private String visitedMunicipality;
    private String email;
    private String password;
    protected Role role;

    public User(){}
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

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    public void HashPassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(this.password);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
