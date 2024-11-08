package com.unicam.Service.Content;

import com.unicam.Entity.Content.Itinerary;
import com.unicam.Repository.Content.ItineraryRepository;

public class ItineraryService {

    private ItineraryRepository repoItinerary;

    public void AddItineray(Itinerary itinerary){
        if(this.Exists(itinerary))
            throw new UnsupportedOperationException("L'itinerario è già presente");
        this.repoItinerary.save(itinerary);
    }

    public void RemoveItinerary(Itinerary itinerary){
        if(!this.Exists(itinerary))
            throw new UnsupportedOperationException("L'itinerario non è presente");
        this.repoItinerary.delete(itinerary);
    }

    private boolean Exists(Itinerary itinerary) {
        if(this.repoItinerary.findByPath(itinerary.getPath()))
            return true;
        else
            return false;
    }
}
