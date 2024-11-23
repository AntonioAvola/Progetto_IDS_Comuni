package com.unicam.Controller;

import com.unicam.DTO.Request.ContentDelete;
import com.unicam.DTO.Request.ContestRequest;
import com.unicam.DTO.Request.EventRequest;
import com.unicam.Entity.CommandPattern.ContestCommand;
import com.unicam.Entity.CommandPattern.EventCommand;
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

        //TODO controllo ruolo

        //controllo presenza GeoPoint
        if(!this.geoPointService.checkGeoPointAlreadyExists(request.getReference(),municipality))
            throw new IllegalArgumentException("Il punto di riferimento non esiste");

        //controllo su inizio e fine
        LocalDateTime now = LocalDateTime.now();
        //TODO controllare anche che non sullo stesso punto non ci sia un evento già approvato con cui si andrebbero ad accavallare quello proposto
        if(!this.eventService.checkDuration(request.getStart(), request.getEnd(), now))
            throw new IllegalArgumentException("Inizio e Fine non conformi");

        User user = this.userService.getUser(idUser);

        EventCommand event = new EventCommand(request, eventService, geoPointService, user);
        event.execute();
    }

    @PostMapping("Api/Animator/AddContest")
    public void AddContest(ContestRequest request){

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

        //TODO controllo ruolo

        User user = this.userService.getUser(idUser);

        //controllo su inizio e fine
        LocalDateTime now = LocalDateTime.now();
        if(!this.contestService.checkDuration(request.getStart(), request.getEnd(), now))
            throw new IllegalArgumentException("Inizio e Fine non conformi");

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

        //TODO controllo ruolo

        User user = this.userService.getUser(idUser);

        if(request.getType().equals("event")){
            if(!this.eventService.getAndRemoveEvent(request.getId(), user))
                throw new IllegalArgumentException("L'evento non rientra tra le proprie attività");
        }
        else if(request.getType().equals("contest")){
           if(!this.contestService.getAndRemoveContest(request.getId(), user))
               throw new IllegalArgumentException("Il contest non rientra tra le proprie attività");
        }
        else
            throw new IllegalArgumentException("Azione non supportata. Assicurati di aver inserito bene la tipologia di attività da eliminare");
    }
}
