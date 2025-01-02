package com.unicam.Controller;

import com.unicam.DTO.Request.ContestRequest;
import com.unicam.DTO.Request.EventRequest;
import com.unicam.DTO.Response.ContestClosedResponse;
import com.unicam.DTO.Response.ContestPartecipants;
import com.unicam.DTO.Response.ContestProgress;
import com.unicam.DTO.Response.Partecipants;
import com.unicam.Entity.CommandPattern.ContestCommand;
import com.unicam.Entity.CommandPattern.EventCommand;
import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/animator")
public class AnimatorController {

    @Autowired
    private ContestService contestService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GeoPointService geoPointService;
    @Autowired
    private UserService userService;

    @PostMapping("/add/event")
    public ResponseEntity<String> Addevent(@RequestBody EventRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

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
        if(!this.geoPointService.checkGeoPointAlreadyExists(request.getReference().toUpperCase(Locale.ROOT),municipality))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Il punto di riferimento non esiste");

        GeoPoint reference = this.geoPointService.getPoint(request.getReference(), municipality);

        //controllo su inizio e fine
        LocalDateTime now = LocalDateTime.now();
        if(!this.eventService.checkDuration(request.getStart(), request.getEnd(), now))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inizio e Fine non conformi");

        /**
         * controllare che sullo stesso punto non ci sia un evento già approvato
         * con cui si andrebbero ad accavallare quello proposto
         */
        if(this.eventService.checkOverlapDuration(request.getStart(), request.getEnd(), reference))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sovrapposizione durata con un evento già approvato per questo riferimento");

        User user = this.userService.getUser(idUser);

        EventCommand event = new EventCommand(request, eventService, geoPointService, user);
        event.execute();
        return ResponseEntity.ok("Proposta di evento inviata con successo");
    }

    @PostMapping("/add/contest")
    public ResponseEntity<String> AddContest(@RequestBody ContestRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        //controllo su inizio e fine
        LocalDateTime now = LocalDateTime.now();
        if(!this.contestService.checkDuration(request.getStart(), request.getEnd(), now))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inizio e Fine non conformi");

        User user = this.userService.getUser(idUser);

        ContestCommand contest = new ContestCommand(request, contestService, user);
        contest.execute();

        return ResponseEntity.ok("Proposta di contest inviata con successo");
    }

    @GetMapping("/contest/closed")
    public ResponseEntity<List<ContestClosedResponse>> getContestClosed(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        this.contestService.updateActivityStatus(LocalDateTime.now());

        List<ContestClosedResponse> closed = this.contestService.getContestNoWinner(municipality, ActivityStatus.FINISHED);
        if(closed.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Al momento non ci sono contest terminati");
        }
        return ResponseEntity.ok(closed);
    }

    @GetMapping("/show/partecipants/of/contest")
    public ResponseEntity<ContestPartecipants> getPartecipantContest(@RequestParam long idContest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        List<Partecipants> partecipants = this.contestService.getPartecipants(idContest);
        if(partecipants.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Non ci sono partecipanti al contest");
        }
        ContestPartecipants partecipantsContest = new ContestPartecipants(idContest, partecipants);
        return ResponseEntity.ok(partecipantsContest);
    }

    @PutMapping("/assign/winner")
    public ResponseEntity<String> assignWinner(@RequestParam long idContest, @RequestParam long idPartecipant){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        User winner = this.userService.getUser(idPartecipant);

        this.contestService.assignWinner(idContest, winner);

        return ResponseEntity.ok("Vincitore assegnato con successo");
    }

    @GetMapping("/contests/progress")
    public ResponseEntity<List<ContestProgress>> getContestsProgress(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        this.contestService.updateActivityStatus(LocalDateTime.now());
        List<ContestProgress> contestProgresses = this.contestService.getContestProgress(municipality);

        return ResponseEntity.ok(contestProgresses);
    }
}
