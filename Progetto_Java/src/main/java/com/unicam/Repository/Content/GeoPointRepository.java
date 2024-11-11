package com.unicam.Repository.Content;

import com.unicam.Entity.Content.GeoPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoPointRepository extends JpaRepository<GeoPoint, Long> {
    boolean existsByNameAndMunicipality(String name, String municipality);

    GeoPoint findByNameAndMunicipality(String reference, String municipality);
}
