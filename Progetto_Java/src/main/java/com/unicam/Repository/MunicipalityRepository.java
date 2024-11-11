package com.unicam.Repository;

import com.unicam.Entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    Municipality findByName(String nameMunicipality);

    boolean existsByName(String name);
}
