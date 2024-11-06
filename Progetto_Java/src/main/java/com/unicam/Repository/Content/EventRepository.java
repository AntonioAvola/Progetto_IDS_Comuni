package com.unicam.Repository.Content;

public interface EventRepository {
    boolean findByTitleAndMunicipality(String title, String municipality);
}
