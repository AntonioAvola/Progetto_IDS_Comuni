package com.unicam.Repository.Content;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Itinerary findById(long idItnerary);

    List<Itinerary> findByMunicipality(String municipality);

    List<Itinerary> findAllByAuthor(User user);

    List<Itinerary> findByMunicipalityAndStatus(String municipality, ContentStatus pending);
}
