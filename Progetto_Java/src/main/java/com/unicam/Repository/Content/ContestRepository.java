package com.unicam.Repository.Content;

import com.unicam.Entity.Content.Contest;

public interface ContestRepository {
    boolean findByTitleAndMunicipality(String title, String municipality);
}
