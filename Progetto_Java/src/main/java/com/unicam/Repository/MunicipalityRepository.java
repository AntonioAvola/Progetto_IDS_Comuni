package com.unicam.Repository;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    boolean existsByName(String name);

    List<Municipality> findAllByStatus(ContentStatus contentStatus);

    Municipality findById(long id);
}
