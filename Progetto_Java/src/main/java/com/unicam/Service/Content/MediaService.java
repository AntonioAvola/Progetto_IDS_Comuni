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

    public Media getMedia(long mediaId) {
        return this.mediaRepository.findById(mediaId);
    }

    public void removeMedia(long id){
        this.mediaRepository.deleteById(id);
    }

    public void deleteMedias(List<Media> medias) {
        for(Media media: medias){
            removeMedia(media.getId());
        }
    }
}
