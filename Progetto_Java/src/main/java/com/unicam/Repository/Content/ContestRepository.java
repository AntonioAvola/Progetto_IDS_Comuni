package com.unicam.Repository.Content;

public interface ContestRepository {
    boolean existsByTitleAndMunicipality(String title, String municipality);
}
