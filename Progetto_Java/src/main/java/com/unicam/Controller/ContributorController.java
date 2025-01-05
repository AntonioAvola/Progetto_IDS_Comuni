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
import com.unicam.Service.Content.MediaService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    private MediaService mediaService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProxyOSM proxy;

    @PostMapping(value = "/add/interestPoint", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Richiesta/Aggiunta punto di interesse",
            description = "Se non c'è un orario di apertura e chiusura da definire, lasciare gli 00. " +
                    "Aggiunta dei file multimediali opzionale")
    public ResponseEntity<String> AddInterestPoint(
            @Parameter(description = "Tipologia di punto di interesse",
                    schema = @Schema(type = "InterestPointType", allowableValues = {"MUSEUM", "HISTORICAL_MONUMENT",
                            "RESTAURANT", "HOTEL", "BED_AND_BREAKFAST", "PARK", "STATUE", "SQUARE"}))
            @RequestParam(defaultValue = "MUSEUM") InterestPointType type,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String reference,
            @Validated @RequestParam(value = "fileUploaded", required = false)
            List<MultipartFile> fileUploaded,
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

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        ContentStatus status = ContentStatus.PENDING;
        //TODO controllo ruolo:
        // se CONTRIBUTOR allora status = PENDING;
        // se CONTRIBUTOR AUTORIZZATO allora status = APPROVED;
        // se qualsiasi altri ruolo, allora eccezione

        InterestPointRequest request = new InterestPointRequest(title, description, reference);
        String address = request.getReference();
        String currentMunicipality = URLEncoder.encode(municipality, StandardCharsets.UTF_8.toString());
        address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);

        //TODO controlla se esiste già il punto geolocalizzato, se esiste, lancia l'eccezione
        if(this.interestPointService.pointAlreadyApproved(request.getReference(), municipality))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esiste già un punto di interesse per questo determinato punto geolocalizzato"); //errore 409 Conflict

        if(role.equals(Role.AUTHORIZED_CONTRIBUTOR.name())){
            this.interestPointService.checkPointPending(request.getReference(), municipality);
            status = ContentStatus.APPROVED;
        }

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
        InterestPointCommand InterestPoint = new InterestPointCommand(request, user, fileUploaded, open, close, type, interestPointService, geoPointService, mediaService, status, coordinates);

        InterestPoint.execute();


        return ResponseEntity.ok("Punto di interesse aggiunto con successo");
    }

    @PostMapping("/add/itinerary")
    @Operation(summary = "Richiesta/Aggiunta itinerario",
            description = "Inserisci le informazioni inerenti all'itinerario che vuoi proporre. " +
                    "Per i punti di interesse usa gli ID disponibili da /get/all/POI/of/own/municipality.")
    public ResponseEntity<String> AddItinerary(@RequestBody ItineraryRequest request) {

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

        ContentStatus status = ContentStatus.PENDING;
        //TODO controllo ruolo:
        // se CONTRIBUTOR allora status = PENDING;
        // se CONTRIBUTOR AUTORIZZATO allora status = APPROVED;
        // se qualsiasi altri ruolo, allora eccezione

        //controlla lunghezza lista punti di interesse, se non ha lunghezza minima 2 o ci sono punti di interesse ripetuti (true), lancia l'eccezione
        if(!this.itineraryService.checkPathLength(request.getPath()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nella lista di punti di interesse sono presenti meno di due punti di interesse");

        //TODO controllo dei punti

        List<InterestPoint> list = this.interestPointService.getList(request.getPath());
        if(this.itineraryService.ItineraryAlreadyExists(list, municipality)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esiste già questo itinerario");
        }
        if(role.equals(Role.AUTHORIZED_CONTRIBUTOR.name())){
            this.itineraryService.deleteItineraryPending(list, municipality);
            status = ContentStatus.APPROVED;
        }

        User user = this.userService.getUser(idUser);

        ItineraryCommand Itinerary = new ItineraryCommand(request, itineraryService, interestPointService, user, status);

        Itinerary.execute();

        return ResponseEntity.ok("Itinerario aggiunto con successo");
    }

    @GetMapping("/get/all/POI/of/own/municipality")
    @Operation(summary = "Visualizza tutti i punti di interesse approvati del comune, con relativo ID")
    public ResponseEntity<List<InterestPointResponse>> getAllPOIofOwnMunicipality(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();


        List<InterestPointResponse> response = this.interestPointService.getPoint(municipality, ContentStatus.APPROVED);
        response.addAll(this.interestPointService.getPoint(municipality, ContentStatus.REPORTED));
        if(response.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Al momento non sono presenti punti di interesse");
        }

        return ResponseEntity.ok(response);
    }
}
