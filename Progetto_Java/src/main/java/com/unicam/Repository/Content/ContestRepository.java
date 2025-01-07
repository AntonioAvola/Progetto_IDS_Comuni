package com.unicam.Repository.Content;

import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    List<Contest> findAllByAuthor(User user);

    List<Contest> findByStatus(ContentStatus approved);

    Contest findById(long id);

    List<Contest> findByMunicipalityAndStatus(String municipality, ContentStatus pending);

    List<Contest> findByMunicipalityAndActivityStatus(String municipality, ActivityStatus finished);

    List<Contest> findByMunicipalityAndActivityStatusAndWinnerName(String municipality, ActivityStatus finished, String s);
}
