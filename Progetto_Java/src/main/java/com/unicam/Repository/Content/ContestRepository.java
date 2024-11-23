package com.unicam.Repository.Content;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    boolean existsByTitleAndMunicipality(String title, String municipality);

    Contest findByTitleAndAuthor(String title, User author);

    List<Contest> findAllByAuthor(User user);

    List<Contest> findByStatus(ContentStatus approved);

    boolean existsByTitleAndAuthor(String title, User author);
}
