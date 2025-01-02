package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.*;
import com.unicam.Entity.User;
import com.unicam.Service.Content.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ReviewBuilder implements Builder{

    private Review review = new Review();

    private MediaService mediaService;

    public ReviewBuilder(MediaService mediaService){
        this.mediaService = mediaService;
    }

    @Override
    public void buildTitle(String title) {
        this.review.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        this.review.setDescription(description);
    }

    @Override
    public void buildAuthor(User author) {
        this.review.setAuthor(author);
    }

    @Override
    public void buildStatus(ContentStatus status) {
        this.review.setStatus(status);
    }

    @Override
    public void buildMunicipality(String municipality) {
        this.review.setMunicipality(municipality);
    }

    public void buildReference(InterestPoint reference){
        this.review.setReference(reference);
    }

    public void buildFile(List<MultipartFile> fileUploaded) throws IOException {
        for(MultipartFile file: fileUploaded){
            if (file != null && !file.isEmpty()) {
                Media media = new Media(file.getOriginalFilename(), file.getBytes(), file.getContentType());
                this.mediaService.save(media);
                this.review.setMedias(media); // Metodo per aggiungere contenuto all'entità
            }
        }
    }

    public Review result(){
        return this.review;
    }
}
