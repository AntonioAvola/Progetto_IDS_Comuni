package com.unicam.Repository.Content;

import com.unicam.Entity.Content.Event;
import com.unicam.Entity.Content.GeoPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitleAndMunicipality(String title, String municipality);

    Event findById(long idEvent);

    List<Event> findByReference(GeoPoint reference);
}
