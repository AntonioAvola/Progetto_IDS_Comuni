package com.unicam.Repository.Content;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findById(long idEvent);

    List<Event> findByReference(GeoPoint reference);

    List<Event> findAllByAuthor(User user);

    List<Event> findByStatus(ContentStatus approved);

    List<Event> findAllByReferenceAndStatus(GeoPoint reference, ContentStatus approved);

    List<Event> findByMunicipalityAndStatus(String municipality, ContentStatus pending);
}
