package com.unicam.Repository.Content;

import com.unicam.Entity.Content.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitleAndMunicipality(String title, String municipality);

    Event findById(long idEvent);
}
