package com.unicam.Service.Content;

import com.unicam.Entity.Content.Event;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
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

    public void checkEvent(GeoPoint reference) {
        List<Event> events = this.repoEvent.findByReference(reference);
        if(!events.isEmpty()){
            this.repoEvent.deleteAll(events);
        }
    }

    public Event getEvent(String title, long userId) {
        Event event = this.repoEvent.findByTitle(title);
        if(event.getAuthor().getId() != userId)
            throw new IllegalArgumentException("Non puoi eliminare questa attività. Non è tra quelle da te inserite");
        return event;
    }

    public void removeEventUser(User user) {
        List<Event> event = this.repoEvent.findAllByAuthor(user);
        this.repoEvent.deleteAll(event);
    }

    public boolean checkTitle(String title, String municipality) {
        return this.repoEvent.existsByTitleAndMunicipality(title, municipality);
    }

    public boolean checkDuration(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if(start.isBefore(end) && start.isAfter(now)){
            return true;
        }
        return false;
    }
}
