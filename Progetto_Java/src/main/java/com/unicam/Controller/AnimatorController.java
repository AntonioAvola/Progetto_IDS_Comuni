package com.unicam.Controller;

import com.unicam.DTO.Request.ContestRequest;
import com.unicam.DTO.Request.EventRequest;
import com.unicam.Entity.CommandPattern.ContestCommand;
import com.unicam.Entity.CommandPattern.EventCommand;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        GeoPoint reference = this.geoPointService.getPoint(request.getReference(), municipality);

        //controllo su inizio e fine
        LocalDateTime now = LocalDateTime.now();
        if(!this.eventService.checkDuration(request.getStart(), request.getEnd(), now))
            throw new IllegalArgumentException("Inizio e Fine non conformi");

        /**
         * controllare anche che sullo stesso punto non ci sia un evento già approvato
         * con cui si andrebbero ad accavallare quello proposto
         */
        if(this.eventService.checkOverlapDuration(request.getStart(), request.getEnd(), reference))
            throw new IllegalArgumentException("Sovrapposizione durata con un evento già approvato per questo riferimento");

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
    public ResponseEntity<String> DeleteActivity(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"EVENT", "CONTEST"}))
            @RequestParam(defaultValue = "EVENT") String type,
            @RequestParam long idContent){

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

        if(type.equals("EVENT")){
            if(!this.eventService.getAndRemoveEvent(idContent, user))
                throw new IllegalArgumentException("L'evento non rientra tra le proprie attività");
        }
        else{
           if(!this.contestService.getAndRemoveContest(idContent, user))
               throw new IllegalArgumentException("Il contest non rientra tra le proprie attività");
        }
        return ResponseEntity.ok("Eliminazione attività eseguita con successo");
    }
}
