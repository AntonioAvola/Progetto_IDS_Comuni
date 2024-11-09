package com.unicam.Repository.Content;

import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Content.InterestPoint;

public interface InterestPointRepository {
    boolean existsByReference(GeoPoint reference);

    InterestPoint[] findByMunicipality(String municipality);

    InterestPoint findById(long idInterestPoint);
}
