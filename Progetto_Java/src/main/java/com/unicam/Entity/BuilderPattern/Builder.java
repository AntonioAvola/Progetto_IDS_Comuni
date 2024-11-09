package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.User;

public interface Builder {

    void buildTitle(String title);

    void buildDescription(String description);

    void buildAuthor(User author);

    void buildStatus(ContentStatus status);

    void buildMunicipality(String municipality);

}
