package com.unicam.Controller;

import com.unicam.DTO.ContentDelete;
import com.unicam.DTO.Request.ContestRequest;
import com.unicam.DTO.Request.EventRequest;
import com.unicam.Entity.CommandPattern.ContestCommand;
import com.unicam.Entity.CommandPattern.EventCommand;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(name = "Api/Animator")
public class AnimatorController {

    @Autowired
    private ContestService contestService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GeoPointService geoPointService;
    @Autowired
    private UserService userService;

    @PostMapping("Api/Animator/AddEvent")
    public void Addevent(EventRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo presenza GeoPoint
        if(!this.geoPointService.checkGeoPointAlreadyExists(request.getReference(),municipality))
            throw new IllegalArgumentException("Il punto di riferimento non esiste");

        //TODO controllo presenza title
        if(this.eventService.checkTitle(request.getTitle(), municipality))
            throw new IllegalArgumentException("Il titolo è già utilizzato");

        //TODO controllo su inizio e fine
        LocalDateTime now = LocalDateTime.now();
        if(!this.eventService.checkDuration(request.getStart(), request.getEnd(), now))
            throw new IllegalArgumentException("Inizio e Fine non conformi");

        User user = this.userService.getUser(idUser);

        EventCommand event = new EventCommand(request, eventService, geoPointService, user);
        event.execute();
    }

    @PostMapping("Api/Animator/AddContest")
    public void AddContest(ContestRequest request){
        //TODO controllo autorizzazioni
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        User user = this.userService.getUser(idUser);

        //TODO controllo su inizio e fine
        //TODO controllo presenza title
        ContestCommand contest = new ContestCommand(request, contestService, user);
        contest.execute();
    }

    @DeleteMapping("Api/Animator/DeleteActivity")
    public void DeleteActivity(ContentDelete request){
        //TODO controllo autorizzazioni
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        User user = this.userService.getUser(idUser);

        if(request.getType().equals("event")){
            Event event = this.eventService.getEvent(request.getTitle(), user.getId());
            this.eventService.removeEvent(event);
        }
        else if(request.getType().equals("contest")){
            Contest contest = this.contestService.getContest(request.getTitle(), user.getId());
            this.contestService.removeContest(contest);
        }
        else
            throw new IllegalArgumentException("Azione non supportata. Assicurati di aver inserito bene la tipologia di attività da eliminare");
    }
}
