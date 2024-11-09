package com.unicam.Repository.Content;

import com.unicam.Entity.Content.Event;

public interface EventRepository {
    boolean existsByTitleAndMunicipality(String title, String municipality);

    Event findById(long idEvent);
}
