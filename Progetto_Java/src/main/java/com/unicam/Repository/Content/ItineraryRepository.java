package com.unicam.Repository.Content;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;

import java.util.List;

public interface ItineraryRepository {
    boolean existsByPath(List<InterestPoint> path);

    Itinerary findById(long idItnerary);
}
