package com.unicam.Repository.Content;

import com.unicam.Entity.Content.GeoPoint;

public interface InterestPointRepository {
    boolean findByReference(GeoPoint reference);
}
