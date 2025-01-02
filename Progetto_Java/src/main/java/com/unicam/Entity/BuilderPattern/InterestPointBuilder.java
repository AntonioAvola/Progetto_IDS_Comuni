package com.unicam.Entity.BuilderPattern;

import com.unicam.Entity.Content.*;
import com.unicam.Entity.User;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.Content.MediaService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class InterestPointBuilder implements Builder {

    private InterestPoint interestPoint = new InterestPoint();
    private GeoPointService geoPointService;
    private MediaService mediaService;

    public InterestPointBuilder(GeoPointService geoPointService,
                                MediaService mediaService) {
        this.geoPointService = geoPointService;
        this.mediaService = mediaService;
    }

    @Override
    public void buildTitle(String title) {
        this.interestPoint.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        this.interestPoint.setDescription(description);
    }

    @Override
    public void buildAuthor(User author) {
        this.interestPoint.setAuthor(author);
    }

    @Override
    public void buildStatus(ContentStatus status) {
        this.interestPoint.setStatus(status);
    }

    @Override
    public void buildMunicipality(String municipality) {
        this.interestPoint.setMunicipality(municipality);
    }

    public void buildReference(String name, List<Double> coordinates){
        GeoPoint point = geoPointService.getPoint(name, interestPoint.getMunicipality());
        if(point != null){
             this.interestPoint.setReference(point);
        }else{
            GeoPoint reference = new GeoPoint(name, this.interestPoint.getMunicipality(), coordinates.get(0), coordinates.get(1));
            this.geoPointService.addGeoPoint(reference);
            this.interestPoint.setReference(reference);
        }
    }

    public void buildType(InterestPointType type){
        this.interestPoint.setType(type);
    }

    public void buildOpenClose(LocalTime open, LocalTime close){
        this.interestPoint.setOpen(open);
        this.interestPoint.setClose(close);
    }

    public void buildFile(List<MultipartFile> fileUploaded) throws IOException {
        for(MultipartFile file: fileUploaded){
            if (file != null && !file.isEmpty()) {
                Media media = new Media(file.getOriginalFilename(), file.getBytes(), file.getContentType());
                this.mediaService.save(media);
                this.interestPoint.setMedias(media);
            }
        }
    }

    public InterestPoint result(){
        return this.interestPoint;
    }
}
