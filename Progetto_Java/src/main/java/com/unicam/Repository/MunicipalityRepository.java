package com.unicam.Repository;

import com.unicam.Entity.Municipality;

public interface MunicipalityRepository {
    Municipality findByName(String nameMunicipality);
}
