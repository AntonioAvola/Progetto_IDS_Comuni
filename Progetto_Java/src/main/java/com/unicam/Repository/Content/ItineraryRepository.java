package com.unicam.Repository.Content;

import com.unicam.Entity.Content.InterestPoint;

import java.util.List;

public interface ItineraryRepository {
    boolean findByPath(List<InterestPoint> path);
}
