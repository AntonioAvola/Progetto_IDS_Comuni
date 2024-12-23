package com.unicam.Controller;

import com.unicam.DTO.Request.ItineraryRequest;
import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.Entity.CommandPattern.InterestPointCommand;
import com.unicam.Entity.CommandPattern.ItineraryCommand;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.InterestPointType;
import com.unicam.Entity.Role;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("api/contributor")
public class ContributorController {
    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private GeoPointService geoPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProxyOSM proxy;

    @PostMapping("/add/interestPoint")
    public ResponseEntity<String> AddInterestPoint(
            @Parameter(description = "Tipologia di punto di interesse",
                    schema = @Schema(type = "InterestPointType", allowableValues = {"MUSEUM", "HISTORICAL_MONUMENT",
                            "RESTAURANT", "HOTEL", "BED_AND_BREAKFAST", "PARK", "STATUE", "SQUARE"}))
            @RequestParam(defaultValue = "MUSEUM") InterestPointType type,
            @RequestBody InterestPointRequest request,
            @Parameter(description = "Ora di inizio")
            @RequestParam(defaultValue = "00") String openHour,
            @Parameter(description = "Minuti di inizio")
            @RequestParam(defaultValue = "00")String openMinute,
            @Parameter(description = "Ora di fine")
            @RequestParam(defaultValue = "00")String closeHour,
            @Parameter(description = "Minuti di fine")
            @RequestParam(defaultValue = "00")String closeMinute) throws IOException {

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

        ContentStatus status = ContentStatus.PENDING;
        //TODO controllo ruolo:
        // se CONTRIBUTOR allora status = PENDING;
        // se CONTRIBUTOR AUTORIZZATO allora status = APPROVED;
        // se qualsiasi altri ruolo, allora eccezione

        String address = request.getReference();
        String currentMunicipality = URLEncoder.encode(municipality, StandardCharsets.UTF_8.toString());
        address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);

        //TODO controlla se esiste già il punto geolocalizzato, se esiste, lancia l'eccezione
        if(this.interestPointService.checkPointAlreadyApproved(request.getReference(), municipality))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esiste già un punto di interesse per questo determinato punto geolocalizzato"); //errore 409 Conflict

        User user = this.userService.getUser(idUser);
        LocalTime open;
        LocalTime close;
        if(!openHour.equals("00") && !closeHour.equals("00")) {
            open = LocalTime.parse(openHour + ":" + openMinute + ":00.000");
            close = LocalTime.parse(closeHour + ":" + closeMinute + ":00.000");
        }
        else{
            open = null;
            close = null;
        }
        InterestPointCommand InterestPoint = new InterestPointCommand(request, user, open, close, type, interestPointService, geoPointService, status, coordinates);
        if(role.equals(Role.AUTHORIZED_CONTRIBUTOR.name())){
            this.interestPointService.checkPointAlreadyApproved(request.getReference(), municipality);
        }
        InterestPoint.execute();

        return ResponseEntity.ok("Punto di interesse aggiunto con successo");
    }

    @PostMapping("/add/itinerary")
    public ResponseEntity<String> AddItinerary(ItineraryRequest request) {

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

        ContentStatus status = ContentStatus.PENDING;
        //TODO controllo ruolo:
        // se CONTRIBUTOR allora status = PENDING;
        // se CONTRIBUTOR AUTORIZZATO allora status = APPROVED;
        // se qualsiasi altri ruolo, allora eccezione

        //controlla lunghezza lista punti di interesse, se non ha lunghezza minima 2 o ci sono punti di interesse ripetuti (true), lancia l'eccezione
        if(!this.itineraryService.checkPathLength(request.getPath()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nella lista di punti di interesse sono presenti meno di due punti di interesse");

        //TODO controllo dei punti

        User user = this.userService.getUser(idUser);

        ItineraryCommand Itinerary = new ItineraryCommand(request, itineraryService, interestPointService, user, ContentStatus.PENDING);
        if(role.equals(Role.AUTHORIZED_CONTRIBUTOR.name())){
            List<InterestPoint> list = this.interestPointService.getList(request.getPath());
            this.itineraryService.deleteItineraryPending(list, municipality);
        }
        Itinerary.execute();

        return ResponseEntity.ok("Itinerario aggiunto con successo");
    }

    @GetMapping("/get/all/POI/of/own/municipality")
    public ResponseEntity<List<InterestPointResponse>> getAllPOIofOwnMunicipality(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();


        List<InterestPointResponse> response = this.interestPointService.getPoint(municipality, ContentStatus.APPROVED);

        return ResponseEntity.ok(response);
    }

    /*@DeleteMapping ("Api/Contributor/DeleteContent")
    public ResponseEntity<String> DeleteContent(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY"}))
            @RequestParam(defaultValue = "INTEREST POINT") String type,
            @RequestParam long idContent) {

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

        if (type.equals("INTEREST POINT")){
            if(!this.interestPointService.getAndRemoveInterestPoint(idContent, user))
                throw new IllegalArgumentException("Il punto di interesse non rientra tra i tuoi contenuti");
        }
        else {
            if(this.itineraryService.getAndRemoveItinerary(idContent, user))
                throw new IllegalArgumentException("L'itinerario non rientra tra i tuoi contenuti");
        }
        return ResponseEntity.ok("Eliminazione del contenuto eseguita con successo");
    }*/
}
