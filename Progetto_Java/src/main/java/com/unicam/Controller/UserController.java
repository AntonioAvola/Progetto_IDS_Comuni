package com.unicam.Controller;

import com.unicam.DTO.Response.*;
import com.unicam.Entity.Content.*;
import com.unicam.Entity.Role;
import com.unicam.Entity.RolePromotion;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.*;
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

import java.time.LocalDateTime;
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
    //@Autowired
    private ReviewService reviewService;

    @GetMapping("/get/all/municipalities")
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

    @GetMapping("/get/own/contents")
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
    @DeleteMapping("/delete/own/content")
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

        //TODO controllo ruolo

        User user = this.userService.getUser(idUser);

        if (type.equals("INTEREST POINT")){
            if(!this.interestPointService.getAndRemoveInterestPoint(idContent, user))
                throw new IllegalArgumentException("Il punto di interesse non rientra tra i tuoi contenuti");
        }
        else if(type.equals("ITINERARY")){
            if(!this.itineraryService.getAndRemoveItinerary(idContent, user))
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

    @PostMapping("/role/promotion/request")
    public ResponseEntity<String> PromotionRequest(
            @Parameter(description = "Ruolo",
                    schema = @Schema(type = "Role", allowableValues = {"CURATOR","CONTRIBUTOR",
                            "AUTHORIZED_CONTRIBUTOR", "ANIMATOR", "MUNICIPALITY_MANAGER"}))
            @RequestParam(defaultValue = "CONTRIBUTOR") Role newRole,
            @RequestParam String justification){

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
        RolePromotion promotion = new RolePromotion(user, newRole, municipality, justification);
        this.promotionService.addPromotion(promotion);

        return ResponseEntity.ok("Richiesta di promozione inviata");
    }

    @GetMapping("/contest/available")
    public ResponseEntity<List<ContestResponse>> getAllContestAvailable(){

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

        this.contestService.updateActivityStatus(LocalDateTime.now());
        User user = this.userService.getUser(idUser);
        List<ContestResponse> contestsAvailable = this.contestService.getContestAvailableNoPartecipated(municipality, user);

        return ResponseEntity.ok(contestsAvailable);
    }

    @PutMapping("/partecipate/contest")
    public ResponseEntity<String> partecipateContest(@RequestParam long idContest){

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

        User partecipant = this.userService.getUser(idUser);
        this.contestService.partecipateContest(idContest, partecipant);
        return ResponseEntity.ok("Partecipazione aggiunta con successo");
    }


    @GetMapping("/viewAllContentByMunicipality")
    public ResponseEntity<ContentOrActivity> viewAllContentByMunicipality(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        List<InterestPointResponse> responsePOI = this.interestPointService.getPoint(visitedMunicipality, ContentStatus.APPROVED);
        responsePOI.addAll(this.interestPointService.getPoint(visitedMunicipality, ContentStatus.REPORTED));
        List<ItineraryResponse> responseItinerary = this.itineraryService.getItinerary(visitedMunicipality, ContentStatus.APPROVED);
        responseItinerary.addAll(this.itineraryService.getItinerary(visitedMunicipality, ContentStatus.REPORTED));
        List<EventResponse> responseEvent = this.eventService.getEvent(visitedMunicipality, ContentStatus.APPROVED);
        List<ContestResponse> responseContest = this.contestService.getContestAvailable(visitedMunicipality);

        ContentOrActivity contentOrActivity = new ContentOrActivity();
        contentOrActivity.getContents().put("InterestPoint", responsePOI);
        contentOrActivity.getContents().put("Itinerary", responseItinerary);
        contentOrActivity.getContents().put("Event", responseEvent);
        contentOrActivity.getContents().put("Contest", responseContest);

        return ResponseEntity.ok(contentOrActivity);
    }

    @PutMapping("/visited/municipality")
    public ResponseEntity <String> visitedMunicipality(@RequestParam String newMunicipality){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        this.userService.visitMunicipality(newMunicipality, idUser);
        return ResponseEntity.ok("Visita il comune eseguita con successo");
    }

    @PutMapping("/reported")
    public ResponseEntity <String> reportContent(
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

        if(type.equals("INTEREST POINT")) {
            this.interestPointService.reportPOI(idContent);
        }
        else {
            this.itineraryService.reportItinerary(idContent);
        }
        return ResponseEntity.ok("Segnalazione eseguita con successo");
    }

    @GetMapping("/ReviewSinglePoint")
    public ResponseEntity <List<Review>> ReviewSinglePoint(@RequestParam long idPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        List <Review> Response = this.reviewService.GetReviewSinglePoint(idPoint);
        return ResponseEntity.ok(Response);
    }

    @GetMapping("/ContestIParticipated")
    public  ResponseEntity <List<Contest>> contestIPartecipated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        User user = this.userService.getUser(idUser);
        List <Contest> contestResponse = this.contestService.getContestPartecipated(user);
        return ResponseEntity.ok(contestResponse);
    }
    @DeleteMapping("api/user/delete/account")
    public ResponseEntity<String> deleteAccount(){
        //TODO chiamare il metodo del servizio dell'utente e passare l'id dell'utente
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        this.userService.deleteAccount(idUser);

        return ResponseEntity.ok("Account eliminato con successo");
    }


}
