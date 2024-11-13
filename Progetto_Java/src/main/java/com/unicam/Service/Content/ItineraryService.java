package com.unicam.Service.Content;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Repository.Content.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository repoItinerary;

    public void addItineray(Itinerary itinerary){
        if(this.exists(itinerary))
            throw new UnsupportedOperationException("L'itinerario è già presente");
        this.repoItinerary.save(itinerary);
    }

    public void removeItinerary(Itinerary itinerary){
        if(!this.exists(itinerary))
            throw new UnsupportedOperationException("L'itinerario non è presente");
        this.repoItinerary.delete(itinerary);
    }

    private boolean exists(Itinerary itinerary) {
        List<Itinerary> itineraries = this.repoItinerary.findByMunicipality(itinerary.getMunicipality());
        for(Itinerary itineraryFound : itineraries){
            if(itineraryFound.getPath().size() == itinerary.getPath().size()){
                if(itineraryFound.getPath().containsAll(itinerary.getPath()))
                    return true;
            }
        }
        return false;
    }

    public void addFavorite(long idItnerary, long idUser){
        Itinerary itinerary = this.repoItinerary.findById(idItnerary);
        if(itinerary.getIdUserFavorites().contains(idUser))
            throw new IllegalArgumentException("L'itinerario è già presente tra i preferiti");
        itinerary.getIdUserFavorites().add(idUser);
        this.repoItinerary.save(itinerary);
    }

    public void checkItinerary(InterestPoint point) {
        List<Itinerary> allItineraries = this.repoItinerary.findByMunicipality(point.getMunicipality());
        for(Itinerary itinerary : allItineraries){
            if(itinerary.getPath().contains(point)){
                if(itinerary.getPath().size() == 2){
                    this.repoItinerary.delete(itinerary);
                }else{
                    itinerary.getPath().remove(point);
                    this.repoItinerary.save(itinerary);
                }
            }
        }
    }

    public Itinerary getItinerary(String title) {
        return this.repoItinerary.findByTitle(title);
    }
}
