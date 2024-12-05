package com.unicam.Controller;

import com.unicam.DTO.Response.ContestResponse;
import com.unicam.DTO.Response.EventResponse;
import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.DTO.Response.ItineraryResponse;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.Role;
import com.unicam.Entity.RolePromotion;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;
import com.unicam.Service.MunicipalityService;
import com.unicam.Service.PromotionService;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(name = "api/User")

public class UserController {
    @Autowired
    private MunicipalityService municipalityService;
    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private UserService userService;
    @Autowired
    private PromotionService promotionService;

    @GetMapping("/getAllMunicipalities")
    public ResponseEntity <List<String>> GetMunicipality() {
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

        List<String> municipalities = this.municipalityService.getAllMunicipalities();
        return ResponseEntity.ok(municipalities);
    }

    @GetMapping("/GetOwnContents")
    public ResponseEntity <Map<String,List<?>>> GetOwnContents(){
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
        Map<String, List<?>> contents = new HashMap<>();

        if (!municipality.equals(visitedMunicipality)){
            //TODO chiamata al service delle recensioni
        }
        else {
            List <InterestPointResponse> OwnPOI = this.interestPointService.getByUser(user);
            List <ItineraryResponse> OwnItinerary = this.itineraryService.getByUser(user);
            contents.put("interestPoint", OwnPOI);
            contents.put("itinerary", OwnItinerary);
            List <EventResponse> OwnEvent = this.eventService.getByUser(user);
            List <ContestResponse> OwnContest = this.contestService.getByUser(user);
            contents.put("event", OwnEvent);
            contents.put("contest", OwnContest);
        }
        return ResponseEntity.ok(contents);

    }


    //TODO implementare correttamente
    @DeleteMapping("Api/User/DeleteContent")
    public ResponseEntity<String> DeleteContent(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY", "EVENT", "CONTEST"}))
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
        else if(type.equals("ITINERARY")){
            if(this.itineraryService.getAndRemoveItinerary(idContent, user))
                throw new IllegalArgumentException("L'itinerario non rientra tra i tuoi contenuti");
        }
        else if(type.equals("EVENT")){
            if(!this.eventService.getAndRemoveEvent(idContent, user))
                throw new IllegalArgumentException("L'evento non rientra tra le proprie attività");
        }
        else{
            if(!this.contestService.getAndRemoveContest(idContent, user))
                throw new IllegalArgumentException("Il contest non rientra tra le proprie attività");
        }
        return ResponseEntity.ok("Eliminazione del contenuto eseguita con successo");
    }

    @PostMapping("/PromotionRequest")
    public ResponseEntity<String> PromotionRequest(@Parameter(description = "Ruolo",
            schema = @Schema(type = "Role", allowableValues = {"CURATOR","CONTRIBUTOR", "AUTHORIZED_CONTRIBUTOR", "ANIMATOR", "MUNICIPALITY_MANAGER"}))
                                                       @RequestParam(defaultValue = "CONTRIBUTOR") Role newRole){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        User user = this.userService.getUser(idUser);
        if(this.promotionService.checkPromotion(user)){
            throw new IllegalArgumentException("La tua richiesta di promozione è ancora in attesa di validazione!");
        }
        RolePromotion promotion = new RolePromotion(user, newRole, municipality);
        this.promotionService.addPromotion(promotion);

        return ResponseEntity.ok("Richiesta di promozione inviata");
    }
}
