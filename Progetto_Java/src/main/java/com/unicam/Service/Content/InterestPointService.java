package com.unicam.Service.Content;

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
        if(this.exists(point)) {
            this.serviceGeo.removeGeoPoint(point.getReference());
            throw new UnsupportedOperationException("Il punto di interesse è già presente o il titolo è già utilizzato. Provare con un altro titolo");
        }
        this.repoInterest.save(point);
    }

    public void removeInterestPoint(InterestPoint point){
        if(!this.exists(point))
            throw new UnsupportedOperationException("Il punto di interesse non è presente");
        this.serviceEvent.checkEvent(point.getReference());
        this.serviceItinerary.checkItinerary(point);
        this.repoInterest.delete(point);
        this.serviceGeo.removeGeoPoint(point.getReference());
    }

    public boolean getAndRemoveInterestPoint(String title, User author){
        if(!this.repoInterest.existsByTitleAndAuthor(title, author)){
            return false;
        }
        InterestPoint point = this.repoInterest.findByTitleAndAuthor(title, author);
        this.removeInterestPoint(point);
        return true;
    }

    private boolean exists(InterestPoint point) {
        if(this.repoInterest.existsByReference(point.getReference()))
            return true;
        else if(this.repoInterest.existsByTitle(point.getTitle()))
            return true;
        return false;
    }

    public List<InterestPoint> getList(List<String> path, String municipality) {
        List<InterestPoint> points = new ArrayList<>();
        List<InterestPoint> pointsDB = this.repoInterest.findByMunicipality(municipality);
        for(InterestPoint point : pointsDB){
            if(path.contains(point.getReference().getName())){
                points.add(point);
            }
        }
        if(path.size() != points.size())
            throw new IllegalArgumentException("Sono stati inseriti nomi di punti di interesse inesistenti");
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
            this.removeInterestPoint(interestPoint);
        }
    }
}
