package com.unicam.Repository.Content;

public interface GeoPointRepository {
    boolean findByNameAndMunicipality(String name, String municipality);
}
