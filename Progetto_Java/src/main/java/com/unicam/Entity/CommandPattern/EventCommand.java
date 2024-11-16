package com.unicam.Entity.CommandPattern;

import com.unicam.DTO.Request.EventRequest;
import com.unicam.Entity.BuilderPattern.EventBuilder;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.User;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import org.springframework.beans.factory.annotation.Autowired;

public class EventCommand implements Command{

    private EventService eventService;

    private Event event;

    private GeoPointService geoPointService;

    private EventBuilder Builder;

    public EventCommand(EventRequest eventRequest, EventService eventService, GeoPointService geoPointService, User author){
        this.eventService = eventService;
        this.geoPointService = geoPointService;
        this.Builder = new EventBuilder();
        this.Builder.buildAuthor(author);
        this.Builder.buildDescription(eventRequest.getDescription());
        this.Builder.buildTitle(eventRequest.getTitle());
        this.Builder.buildDuration(eventRequest.getStart(), eventRequest.getEnd());
        this.Builder.buildMunicipality(author.getMunicipality());
        this.Builder.buildReference(this.geoPointService.getPoint(eventRequest.getReference(), author.getMunicipality()));
        this.Builder.buildStatus(ContentStatus.PENDING);
        this.event = this.Builder.result();
    }

    @Override
    public void execute() {
        this.eventService.addEvent(event);
    }
}
