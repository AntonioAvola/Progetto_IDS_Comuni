package com.unicam.Service.Content;

import com.unicam.Entity.Content.Event;
import com.unicam.Repository.Content.EventRepository;

public class EventService {

    private EventRepository repoEvent;

    public void addEvent(Event event){
        if(this.exists(event)){
            throw new UnsupportedOperationException("l'evento esiste già");
        }
        this.repoEvent.save(event);
    }

    public void removeEvent(Event event){
        if(!this.exists(event)){
            throw new UnsupportedOperationException("l'evento non esiste");
        }
        this.repoEvent.delete(event);
    }


    private boolean exists(Event event){
        if(this.repoEvent.existsByTitleAndMunicipality(event.getTitle(), event.getMunicipality())){
            return true;
        }else{
            return false;
        }
    }

    public void addFavorite(long idEvent, long idUser){
        Event event = this.repoEvent.findById(idEvent);
        if(event.getIdUserFavorites().contains(idUser))
            throw new IllegalArgumentException("Evento già presente tra i preferiti");
        event.getIdUserFavorites().add(idUser);
        this.repoEvent.save(event);
    }
}
