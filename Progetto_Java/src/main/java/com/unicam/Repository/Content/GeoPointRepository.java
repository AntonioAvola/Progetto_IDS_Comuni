package com.unicam.Repository.Content;

import com.unicam.Entity.Content.GeoPoint;

public interface GeoPointRepository {
    boolean existsByNameAndMunicipality(String name, String municipality);

    GeoPoint findByNameAndMunicipality(String reference, String municipality);
}
