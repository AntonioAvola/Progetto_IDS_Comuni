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
    boolean existsByReference(GeoPoint reference);

    List<InterestPoint> findByMunicipality(String municipality);

    InterestPoint findById(long idInterestPoint);

    InterestPoint findByTitleAndAuthor(String title, User author);

    List<InterestPoint> findAllByAuthor(User user);

    boolean existsByTitle(String title);

    boolean existsByTitleAndAuthor(String title, User author);

    List<InterestPoint> findByMunicipalityAndStatus(String municipality, ContentStatus pending);
}
