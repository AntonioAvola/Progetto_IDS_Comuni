package com.unicam.Service.Content;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Repository.Content.InterestPointRepository;

import java.util.ArrayList;
import java.util.List;

public class InterestPointService {

    private InterestPointRepository repoInterest;
    private GeoPointService serviceGeo;
    private ItineraryService serviceItinerary;
    private EventService serviceEvent;

    public void addInterestPoint(InterestPoint point){
        if(this.exists(point))
            throw new UnsupportedOperationException("Il punto di interesse è già presente");
        this.repoInterest.save(point);
    }

    public void removeInterestPoint(InterestPoint point){
        if(!this.exists(point))
            throw new UnsupportedOperationException("Il punto di interesse non è presente");
        //TODO prima di eliminare il GeoPoint associato controllo che non ci siano eventi collegati a tale punto, altrimenti:
            //TODO 2. eliminare l'evento indipendentemente se approvato o in attesa
        //TODO elimino il punto di interesse stesso dal path dell'itinerario.
            //TODO se l'itinerio ha lunghezza 2 prima della rimozione, eliminare direttamente l'itinerario
        this.serviceGeo.removeGeoPoint(point.getReference());
        this.repoInterest.delete(point);
    }

    private boolean exists(InterestPoint point) {
        if(this.repoInterest.existsByReference(point.getReference()))
            return true;
        else
            return false;
    }

    public List<InterestPoint> getList(List<String> path, String municipality) {
        List<InterestPoint> points = new ArrayList<>();
        for(InterestPoint point : this.repoInterest.findByMunicipality(municipality)){
            if(path.contains(point.getReference().getName())){
                points.add(point);
            }
        }
        return points;
    }

    public void addFavorite(long idInterestPoint, long idUser){
        InterestPoint point = this.repoInterest.findById(idInterestPoint);
        if(point.getIdUserFavorites().contains(idUser))
            throw new IllegalArgumentException("Il punto di interesse è già tra i preferiti");
        point.getIdUserFavorites().add(idUser);
        this.repoInterest.save(point);

    }
}