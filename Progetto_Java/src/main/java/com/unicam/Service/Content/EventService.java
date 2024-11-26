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

    public void removeEvent(long idEvent){
        Event event = this.repoEvent.findById(idEvent);
        this.repoEvent.delete(event);
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
        this.repoEvent.deleteAll(events);
    }

    public boolean getAndRemoveEvent(long idEvent, User author) {
        if(!this.repoEvent.existsByIdAndAuthor(idEvent, author))
            return false;
        this.removeEvent(idEvent);
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

    public boolean checkOverlapDuration(LocalDateTime start, LocalDateTime end, GeoPoint reference) {
        List<Event> events = this.repoEvent.findAllByReferenceAndStatus(reference, ContentStatus.APPROVED);
        for(Event event : events){
            if((start.isAfter(event.getDuration().getStart()) && start.isBefore(event.getDuration().getFinish()))
                    || (end.isAfter(event.getDuration().getStart()) && end.isBefore(event.getDuration().getFinish()))
                    || (start.isBefore(event.getDuration().getStart()) && end.isAfter(event.getDuration().getFinish())))
                return true;
        }
        return false;
    }
}
