package com.unicam.Controller;

import com.unicam.DTO.Request.ItineraryRequest;
import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.CommandPattern.InterestPointCommand;
import com.unicam.Entity.CommandPattern.ItineraryCommand;
import com.unicam.Entity.Content.ContentStatus;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(name = "api/contributor")
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

    @PostMapping("Api/Contributor/addInterestPoint")
    public ResponseEntity<String> AddInterestPoint(InterestPointRequest request) throws IOException {

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
        if(this.geoPointService.checkGeoPointAlreadyExists(request.getReference(), municipality))
            throw new UnsupportedOperationException("Esiste già un punto di interesse per questo determinato punto geolocalizzato");

        User user = this.userService.getUser(idUser);

        InterestPointCommand InterestPoint = new InterestPointCommand(request, user, interestPointService, geoPointService, status, coordinates);
        InterestPoint.execute();

        return ResponseEntity.ok("Punto di interesse aggiunto con successo");
    }

    @PostMapping("Api/Contributor/addItinerary")
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

        //TODO controlla lunghezza lista punti di interesse, se non ha lunghezza minima 2 o ci sono punti di interesse ripetuti (true), lancia l'eccezione
        if(!this.itineraryService.checkPathLength(request.getPath()))
            throw new IllegalArgumentException("Nella lista di punti di interesse sono presenti punti duplicati o meno di due punti di interesse");

        User user = this.userService.getUser(idUser);

        ItineraryCommand Itinerary = new ItineraryCommand(request, itineraryService, interestPointService, user, ContentStatus.PENDING);
        Itinerary.execute();

        return ResponseEntity.ok("Itinerario aggiunto con successo");
    }

    //TODO @ANTONIO non considerare questa API perchè ho controllato solo per il punto di interesse;
    // per l'itinerario devo prima fare bene l'aggiunta
    @DeleteMapping ("Api/Contributor/DeleteContent")
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
    }
}
