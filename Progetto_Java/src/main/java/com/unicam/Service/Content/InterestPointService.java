package com.unicam.Service.Content;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Repository.Content.InterestPointRepository;

public class InterestPointService {

    private InterestPointRepository repoInterest;
    private GeoPointService serviceGeo;
    private ItineraryService serviceItinerary;
    private EventService serviceEvent;

    public void AddInterestPoint(InterestPoint point){
        if(this.Exists(point))
            throw new UnsupportedOperationException("Il punto di interesse è già presente");
        this.repoInterest.save(point);
    }

    public void RemoveInterestPoint(InterestPoint point){
        if(!this.Exists(point))
            throw new UnsupportedOperationException("Il punto di interesse non è presente");
        //TODO prima di eliminare il GeoPoint associato controllo che non ci siano eventi collegati a tale punto, altrimenti:
            //TODO 1. viene mandato un messaggio di errore se l'evento associato a punto è già stato approvato "Impossibile rimuovere il punto"
            //TODO 2. eliminare l'evento indipendentemente se approvato o in attesa
        //TODO elimino il punto di interesse stesso dal path dell'itinerario.
            //TODO se l'itinerio ha lunghezza 2 prima della rimozione, eliminare direttamente l'itinerario
        this.serviceGeo.RemoveGeoPoint(point.getReference());
        this.repoInterest.delete(point);
    }

    private boolean Exists(InterestPoint point) {
        if(this.repoInterest.findByReference(point.getReference()))
            return true;
        else
            return false;
    }
}
