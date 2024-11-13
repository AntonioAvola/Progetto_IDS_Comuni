package com.unicam.Repository.Content;

import com.unicam.Entity.Content.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    boolean existsByTitleAndMunicipality(String title, String municipality);

    Contest findByTitle(String title);
}
