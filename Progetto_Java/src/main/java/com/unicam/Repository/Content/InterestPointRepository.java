package com.unicam.Repository.Content;

import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Content.InterestPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestPointRepository extends JpaRepository<InterestPoint, Long> {
    boolean existsByReference(GeoPoint reference);

    InterestPoint[] findByMunicipality(String municipality);

    InterestPoint findById(long idInterestPoint);

    InterestPoint findByTitleIgnoreCase(String title);
}
