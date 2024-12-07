package com.unicam.Service.Content;

import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
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

    public InterestPointService(InterestPointRepository repoInterest) {
        this.repoInterest = repoInterest;
    }

    public void addInterestPoint(InterestPoint point){
        this.repoInterest.save(point);
    }

    //TODO vedere se metodo funzionante
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

    public List<InterestPoint> getList(List<Long> path /*, String municipality*/) {
        //List<InterestPoint> points = new ArrayList<>();
        List<InterestPoint> points = this.repoInterest.findAllById(path);
        /*List<InterestPoint> pointsDB = this.repoInterest.findByMunicipality(municipality);
        for(InterestPoint point : pointsDB) {
            if (path.contains(point.getId())) {
                points.add(point);
            }
        }*/
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
        }
    }

    private void removeInterestPointPending(InterestPoint point) {
        this.repoInterest.delete(point);
        this.serviceGeo.removeGeoPoint(point.getReference());
    }

    public boolean checkMunicipality(long idContent, String municipality) {
        return this.repoInterest.existsByIdAndMunicipality(idContent, municipality);
    }

    /*public List<InterestPoint> getByUser(User user) {
        return this.repoInterest.findAllByAuthor(user);
    }*/

    public List<InterestPointResponse> getByUser(User user) {
        List<InterestPoint> pois = this.repoInterest.findAllByAuthor(user);
        return convertResponse(pois);
    }
}
