package com.unicam.Service.Content;

import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Media;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.InterestPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterestPointService {

    private final InterestPointRepository repoInterest;
    @Autowired
    private  GeoPointService serviceGeo;
    @Autowired
    private ItineraryService serviceItinerary;
    @Autowired
    private EventService serviceEvent;
    @Autowired
    private MediaService mediaService;

    public InterestPointService(InterestPointRepository repoInterest) {
        this.repoInterest = repoInterest;
    }

    public void addInterestPoint(InterestPoint point){
        this.repoInterest.save(point);
    }

    public void removeInterestPoint(long idPoint){
        InterestPoint point = this.repoInterest.findById(idPoint);
        this.serviceEvent.checkEvent(point.getReference());
        this.serviceItinerary.checkItinerary(point);
        this.removeInterestPointPending(point);
    }

    public boolean getAndRemoveInterestPoint(long idPoint, User author){
        if(!this.repoInterest.existsByIdAndAuthor(idPoint, author)){
            return false;
        }
        this.removeInterestPoint(idPoint);
        return true;
    }

    public List<InterestPoint> getList(List<Long> path) {
        List<InterestPoint> points = this.repoInterest.findAllById(path);
        return points;
    }

    public void addFavorite(long idInterestPoint, long idUser){
        InterestPoint point = this.repoInterest.findById(idInterestPoint);
        if(point.getIdUserFavorites().contains(idUser))
            throw new IllegalArgumentException("Il punto di interesse è già tra i preferiti");
        point.getIdUserFavorites().add(idUser);
        this.repoInterest.save(point);

    }

    public void removeInterestPointUser(User user) {
        List<InterestPoint> interestPoints = this.repoInterest.findAllByAuthor(user);
        for(InterestPoint interestPoint : interestPoints){
            this.removeInterestPoint(interestPoint.getId());
        }
    }

    public List<InterestPointResponse> getPoint(String municipality, ContentStatus pending) {
        List<InterestPoint> points = this.repoInterest.findByMunicipalityAndStatus(municipality, pending);
        return convertResponse(points);
    }

    private List<InterestPointResponse> convertResponse(List<InterestPoint> points) {
        List<InterestPointResponse> response = new ArrayList<>();
        for(InterestPoint point : points){
            InterestPointResponse pointResponse = new InterestPointResponse(point.getId(), point.getTitle(),
                    point.getDescription(), point.getReference().getName(), point.getOpen(), point.getClose());
            response.add(pointResponse);
        }
        return response;
    }

    public void approveOrRejectPoint(long idContent, ContentStatus approved) {
        InterestPoint point = this.repoInterest.findById(idContent);
        if(approved.equals(ContentStatus.REJECTED)) {
            this.removeInterestPointPending(point);
        }
        else{
            point.setStatus(approved);
            this.repoInterest.save(point);
            deletePOIPending(point.getReference());
        }
    }

    private void deletePOIPending(GeoPoint reference) {
        List<InterestPoint> list = this.repoInterest.findAllByReferenceAndStatus(reference, ContentStatus.PENDING);
        List<Media> toBeDelete = new ArrayList<>();
        for(InterestPoint point: list){
            toBeDelete.addAll(point.getMedias());
        }
        this.repoInterest.deleteAll(list);
        this.mediaService.deleteMedias(toBeDelete);
    }


    private void removeInterestPointPending(InterestPoint point) {
        List<Media> medias = new ArrayList<>(point.getMedias());
        this.repoInterest.delete(point);
        if(this.repoInterest.findAllByReferenceAndStatus(point.getReference(), ContentStatus.PENDING).isEmpty()){
            this.serviceGeo.removeGeoPoint(point.getReference());
        }
        this.mediaService.deleteMedias(medias);
    }

    public boolean checkMunicipality(long idContent, String municipality) {
        return this.repoInterest.existsByIdAndMunicipality(idContent, municipality);
    }

    public List<InterestPointResponse> getByUser(User user) {
        List<InterestPoint> pois = this.repoInterest.findAllByAuthor(user);
        return convertResponse(pois);
    }

    public void reportPOI(long idContent) {
        InterestPoint point = this.repoInterest.findById(idContent);
        point.setStatus(ContentStatus.REPORTED);
        this.repoInterest.save(point);
    }

    public InterestPoint GetSinglePoint(long idPoint) {
        return this.repoInterest.findById(idPoint);
    }

    public boolean checkPointAlreadyApproved(String reference, String municipality) {
        GeoPoint geoPoint = this.serviceGeo.getPoint(reference, municipality);
        if(geoPoint == null){
            return false;
        }
        return this.repoInterest.existsByMunicipalityAndReferenceAndStatus(municipality, geoPoint, ContentStatus.APPROVED);

    }
}
