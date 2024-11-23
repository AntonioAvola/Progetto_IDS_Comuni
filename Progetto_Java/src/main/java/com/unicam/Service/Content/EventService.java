package com.unicam.Service.Content;

import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.ContentStatus;
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

    public boolean getAndRemoveEvent(long idEvent, User author) {
        if(!this.repoEvent.existsByIdAndAuthor(idEvent, author))
            return false;
        Event event = this.repoEvent.findById(idEvent);
        this.removeEvent(event);
        return true;
    }

    public void removeEventUser(User user) {
        List<Event> event = this.repoEvent.findAllByAuthor(user);
        this.repoEvent.deleteAll(event);
    }

    public boolean checkDuration(LocalDateTime start, LocalDateTime end, LocalDateTime now) {
        if(start.isAfter(now) && start.isBefore(end)){
            return true;
        }
        return false;
    }

    public void updateActivityStatus(LocalDateTime now) {
        List<Event> events = this.repoEvent.findByStatus(ContentStatus.APPROVED);
        for(Event event : events){
            if(event.getActivityStatus().equals(ActivityStatus.WAITING)){
                if(event.getDuration().getStart().isBefore(now)) {
                    event.setActivityStatus(ActivityStatus.STARTED);
                    this.repoEvent.save(event);
                }
            }
            else if(event.getActivityStatus().equals(ActivityStatus.STARTED)){
                if(event.getDuration().getFinish().isBefore(now)) {
                    event.setActivityStatus(ActivityStatus.FINISHED);
                    this.repoEvent.save(event);
                }
            }
        }
    }
}
