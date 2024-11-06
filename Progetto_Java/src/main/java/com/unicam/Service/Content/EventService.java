package com.unicam.Service.Content;

import com.unicam.Entity.Content.Event;
import com.unicam.Repository.Content.EventRepository;

public class EventService {

    private EventRepository repoEvent;

    public void addEvent(Event event){
        if(this.Exists(event)){
            throw new UnsupportedOperationException("l'evento esiste già");
        }
        this.repoEvent.save(event);
    }

    public void removeEvent(Event event){
        if(!this.Exists(event)){
            throw new UnsupportedOperationException("l'evento non esiste");
        }
        this.repoEvent.delete(event);
    }


    private boolean Exists(Event event){
        if(this.repoEvent.findByTitleAndMunicipality(event.getTitle(), event.getMunicipality())){
            return true;
        }else{
            return false;
        }
    }
}
