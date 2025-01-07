package com.unicam.Service.Content;

import com.unicam.Entity.Content.Media;
import com.unicam.Repository.Content.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    public void save(Media media) {
        this.mediaRepository.save(media);
    }

    public void deleteMedias(List<Media> medias) {
        this.mediaRepository.deleteAll(medias);
    }
}
