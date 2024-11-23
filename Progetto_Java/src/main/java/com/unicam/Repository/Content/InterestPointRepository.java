package com.unicam.Repository.Content;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestPointRepository extends JpaRepository<InterestPoint, Long> {

    List<InterestPoint> findByMunicipality(String municipality);

    InterestPoint findById(long idInterestPoint);

    List<InterestPoint> findAllByAuthor(User user);

    List<InterestPoint> findByMunicipalityAndStatus(String municipality, ContentStatus pending);

    boolean existsByIdAndAuthor(long idPoint, User author);
}
