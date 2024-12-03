package com.unicam.Service.Content;

import com.unicam.DTO.Response.EventResponse;
import com.unicam.Entity.Content.*;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<EventResponse> getEvent(String municipality, ContentStatus pending) {
        List<Event> events = this.repoEvent.findByMunicipalityAndStatus(municipality, pending);
        return convertResponse(events);
    }

    private List<EventResponse> convertResponse(List<Event> events) {
        List<EventResponse> response = new ArrayList<>();
        for(Event event : events){
            EventResponse convert = new EventResponse(event.getId(), event.getTitle(),
                    event.getDescription(), event.getDuration(), event.getReference().getName());
            response.add(convert);
        }
        return response;
    }

    public boolean checkMunicipality(long idContent, String municipality) {
        return this.repoEvent.existsByIdAndMunicipality(idContent, municipality);
    }

    public void approveOrRejectPoint(long idContent, ContentStatus status) {
        if(status.equals(ContentStatus.REJECTED)) {
            this.removeEvent(idContent);
        }
        else{
            Event event = this.repoEvent.findById(idContent);
            event.setStatus(status);
            event.setActivityStatus(ActivityStatus.WAITING);
            removeEventPendingOverlapping(event);
            this.repoEvent.save(event);
        }
    }

    private void removeEventPendingOverlapping(Event event) {
        List<Event> events = this.repoEvent.findAllByReferenceAndStatus(event.getReference(), ContentStatus.PENDING);
        for(Event eventFound : events){
            if(!((eventFound.getDuration().getStart().isBefore(event.getDuration().getStart()) &&
                    eventFound.getDuration().getFinish().isBefore(event.getDuration().getStart())) ||
                    (eventFound.getDuration().getStart().isAfter(event.getDuration().getFinish()) &&
                            eventFound.getDuration().getFinish().isAfter(event.getDuration().getFinish())))){
                this.repoEvent.delete(eventFound);
            }
        }
    }
}
