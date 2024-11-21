package com.unicam.Security;

import com.unicam.Entity.Role;
import com.unicam.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserCustomDetails implements UserDetails {

    private String username;

    private String id;

    private String role;

    private String municipality;

    private String visitedMunicipality;

    private Collection<? extends GrantedAuthority> authorities;

    public UserCustomDetails(String username,String id, String municipality, String role, String visitedMunicipality){
        this.username = username;
        this.id = id;
        this.municipality = municipality;
        this.role = role;
        this.visitedMunicipality = visitedMunicipality;
    }

    public String getId(){
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getVisitedMunicipality() {
        return visitedMunicipality;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
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
}
