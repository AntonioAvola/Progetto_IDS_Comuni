package com.unicam.Service.Content;

import com.unicam.Entity.Content.Itinerary;
import com.unicam.Repository.Content.ItineraryRepository;

public class ItineraryService {

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
        if(this.repoItinerary.existsByPath(itinerary.getPath()))
            return true;
        else
            return false;
    }

    public void addFavorite(long idItnerary, long idUser){
        Itinerary itinerary = this.repoItinerary.findById(idItnerary);
        if(itinerary.getIdUserFavorites().contains(idUser))
            throw new IllegalArgumentException("L'itinerario è già presente tra i preferiti");
        itinerary.getIdUserFavorites().add(idUser);
        this.repoItinerary.save(itinerary);
    }
}
