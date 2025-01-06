package com.unicam.Controller;

import com.unicam.DTO.Request.ContestRequest;
import com.unicam.DTO.Request.EventRequest;
import com.unicam.DTO.Response.*;
import com.unicam.Entity.CommandPattern.ContestCommand;
import com.unicam.Entity.CommandPattern.EventCommand;
import com.unicam.Entity.Content.ActivityStatus;
import com.unicam.Entity.Content.GeoPoint;
import com.unicam.Entity.Role;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

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
    private InterestPointService interestPointService;
    @Autowired
    private UserService userService;

    //TODO rivedere per il punto geolocalizzzato; se usare direttamente l'ID e quindi
    // creare una API di visualizzazione di tutti i punti geolocalizzati che hanno un punto di interesse approvato
    @PostMapping("/add/event")
    @Operation(summary = "Proponi un evento")
    public ResponseEntity<String> Addevent(@RequestBody EventRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        GeoPoint reference = this.geoPointService.getById(request.getIdReference());

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

        EventCommand event = new EventCommand(request, eventService, geoPointService, user, reference);
        event.execute();
        return ResponseEntity.ok("Proposta di evento inviata con successo");
    }

    @PostMapping("/add/contest")
    @Operation(summary = "Proponi un contest")
    public ResponseEntity<String> AddContest(@RequestBody ContestRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

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
    @Operation(summary = "Visualizza contest terminati",
            description = "Visualizza solamente i contest terminati, a cui però non è ancora stato assegnato un vincitore.")
    public ResponseEntity<List<ContestClosedResponse>> getContestClosed(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        this.contestService.updateActivityStatus(LocalDateTime.now());

        List<ContestClosedResponse> closed = this.contestService.getContestNoWinner(municipality, ActivityStatus.FINISHED);
        if(closed.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Al momento non ci sono contest terminati");
        }
        return ResponseEntity.ok(closed);
    }

    @GetMapping("/show/partecipants/of/contest")
    @Operation(summary = "Visualizza partecipanti contest terminati",
            description = "Visualizza la lista di tutti i partecipanti di un determinato contest terminato. " +
                    "Usa gli ID disponibili da /contest/closed.")
    public ResponseEntity<ContestPartecipants> getPartecipantContest(@RequestParam long idContest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        List<Partecipants> partecipants = this.contestService.getPartecipants(idContest);
        if(partecipants.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Non ci sono partecipanti al contest");
        }
        ContestPartecipants partecipantsContest = new ContestPartecipants(idContest, partecipants);
        return ResponseEntity.ok(partecipantsContest);
    }

    @PutMapping("/assign/winner")
    @Operation(summary = "Assegna vincitore",
            description = "Proclama un partecipante come vincitore del contest. " +
                    "Usa gli ID disponibili rispettivamente da '/contest/closed' e '/show/partecipants/of/contest'.")
    public ResponseEntity<String> assignWinner(@RequestParam long idContest, @RequestParam long idPartecipant){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        User winner = this.userService.getUser(idPartecipant);

        this.contestService.assignWinner(idContest, winner);

        return ResponseEntity.ok("Vincitore assegnato con successo");
    }

    @GetMapping("/contests/progress")
    @Operation(summary = "Visualizza andamento contest",
            description = "Visualizza lo stato di avanzamento di tutti i contest del proprio comune")
    public ResponseEntity<List<ContestProgress>> getContestsProgress(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        this.contestService.updateActivityStatus(LocalDateTime.now());
        List<ContestProgress> contestProgresses = this.contestService.getContestProgress(municipality);

        return ResponseEntity.ok(contestProgresses);
    }

    @GetMapping("/get/all/geopoint")
    @Operation(summary = "Visualizza tutti i punti geolocalizzati",
            description = "Restituisce una lista di punti geolocalizzati associati a punti di interesse approvati con i relativi ID.")
    public ResponseEntity<List<GeoPointResponse>> getAllGeoPoint(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(!municipality.equals(visitedMunicipality) || !role.equals(Role.ANIMATOR.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        List<GeoPointResponse> list = this.interestPointService.getAllReference(municipality);

        if(list.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Non ci sono punti geolocalizzati");
        }

        return ResponseEntity.ok(list);
    }
}



