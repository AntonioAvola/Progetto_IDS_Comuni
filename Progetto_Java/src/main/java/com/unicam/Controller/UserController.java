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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/user")

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
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/get/all/municipalities")
    @Operation(summary = "Visualizza tutti i comuni presenti nella piattaforma",
            description = "Visualizza la lista di tutti i comuni approvati nella piattaforma, restituendo il nome")
    public ResponseEntity<List<String>> GetMunicipality() {
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

        List<String> municipalities = this.municipalityService.getAllMunicipalities();
        return ResponseEntity.ok(municipalities);
    }

    @GetMapping("/get/own/contents")
    @Operation(summary = "Visualizza solamente i propri contenuti, mostrando il relativo ID")
    public ResponseEntity<Map<String,List<?>>> GetOwnContents(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();


        if(role.equals(Role.ADMIN.name()) || role.equals(Role.MUNICIPALITY_MANAGER.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo
        User user = this.userService.getUser(idUser);
        Map<String, List<?>> contents = new HashMap<>();

        if (!municipality.equals(visitedMunicipality)){
            List<ReviewResponse> ownReviews = this.reviewService.getByUser(user, visitedMunicipality);
            if(!ownReviews.isEmpty()){
                contents.put( visitedMunicipality + " reviews", ownReviews);
            }
        }
        else {
            List <InterestPointResponse> ownPOI = this.interestPointService.getByUser(user);
            List <ItineraryResponse> ownItinerary = this.itineraryService.getByUser(user);
            if(!ownPOI.isEmpty()){
                contents.put("interestPoint", ownPOI);
            }
            if(!ownItinerary.isEmpty()){
                contents.put("itinerary", ownItinerary);
            }
            List <EventResponse> ownEvent = this.eventService.getByUser(user);
            List <ContestResponse> ownContest = this.contestService.getByUser(user);
            if(!ownEvent.isEmpty()){
                contents.put("event", ownEvent);
            }
            if(!ownContest.isEmpty()){
                contents.put("contest", ownContest);
            }
        }
        if(contents.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Non hia inserito alcun contenuto, attività o recensione");
        }
        return ResponseEntity.ok(contents);

    }

    @DeleteMapping("/delete/own/content")
    @Operation(summary = "Elimina un proprio contenuto",
            description = "Elimina un tuo contenuto. Usa uno degli ID disponibili da /get/own/contents.")
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
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(!municipality.equals(visitedMunicipality) || role.equals(Role.ADMIN.name()) || role.equals(Role.MUNICIPALITY_MANAGER.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        User user = this.userService.getUser(idUser);

        if (type.equals("INTEREST POINT")){
            this.interestPointService.removeInterestPoint(idContent);
        }
        else if(type.equals("ITINERARY")){
            this.itineraryService.removeItinerary(idContent);
        }
        else if(type.equals("EVENT")){
            this.eventService.removeEvent(idContent);
        }
        else {
            this.contestService.removeContest(idContent);
        }
        return ResponseEntity.ok("Eliminazione del contenuto eseguita con successo");
    }

    @PostMapping("/role/promotion/request")
    @Operation(summary = "Richiedi la promozione ad un ruolo differente dal tuo attuale")
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
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(!municipality.equals(visitedMunicipality) || role.equals(Role.ADMIN.name()) || role.equals(Role.MUNICIPALITY_MANAGER.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        User user = this.userService.getUser(idUser);
        if(this.promotionService.checkPromotion(user)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La tua richiesta di promozione è ancora in attesa di validazione!");
        }
        RolePromotion promotion = new RolePromotion(user, newRole, municipality, justification);
        this.promotionService.addPromotion(promotion);

        return ResponseEntity.ok("Richiesta di promozione inviata");
    }

    @GetMapping("/contest/available")
    @Operation(summary = "Visualizza tutti i contest non ancora iniziati, a cui non si è ancora partecipato, con relativo ID")
    public ResponseEntity<List<ContestResponse>> getAllContestAvailable(){

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

        this.contestService.updateActivityStatus(LocalDateTime.now());
        User user = this.userService.getUser(idUser);
        List<ContestResponse> contestsAvailable = this.contestService.getContestAvailableNoPartecipated(municipality, user);

        return ResponseEntity.ok(contestsAvailable);
    }

    @PutMapping("/partecipate/contest")
    @Operation(summary = "Partecipa ad un contest",
            description = "Partecipa ad un contest al momento disponibile. " +
                    "Usa uno degli ID disponibili da /contest/available.")
    public ResponseEntity<String> partecipateContest(@RequestParam long idContest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if((municipality.equals(visitedMunicipality) && role.equals(Role.ANIMATOR.name()) || role.equals(Role.ADMIN.name()) || role.equals(Role.MUNICIPALITY_MANAGER.name())))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        User partecipant = this.userService.getUser(idUser);
        this.contestService.partecipateContest(idContest, partecipant);
        return ResponseEntity.ok("Partecipazione aggiunta con successo");
    }


    @GetMapping("/view/all/content/by/municipality")
    @Operation(summary = "Visualizza tutti i contenuti nella piattaforma relativi al comune (approvati e segnalati), con relativo ID")
    public ResponseEntity<ContentOrActivity> viewAllContentByMunicipality(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(role.equals(Role.ADMIN.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        this.eventService.updateActivityStatus(LocalDateTime.now());
        this.contestService.updateActivityStatus(LocalDateTime.now());

        List<InterestPointWithReviewNum> responsePOI = this.interestPointService.getInterestPoint(visitedMunicipality, ContentStatus.APPROVED);
        responsePOI.addAll(this.interestPointService.getInterestPoint(visitedMunicipality, ContentStatus.REPORTED));
        List<ItineraryResponse> responseItinerary = this.itineraryService.getItinerary(visitedMunicipality, ContentStatus.APPROVED);
        responseItinerary.addAll(this.itineraryService.getItinerary(visitedMunicipality, ContentStatus.REPORTED));
        List<EventResponse> responseEvent = this.eventService.getEvent(visitedMunicipality, ContentStatus.APPROVED);
        List<ContestResponse> responseContest = this.contestService.getContestAvailable(visitedMunicipality);

        ContentOrActivity contentOrActivity = new ContentOrActivity();
        if(!responsePOI.isEmpty()){
            contentOrActivity.getContents().put("interest point", responsePOI);
        }
        if(!responseItinerary.isEmpty()){
            contentOrActivity.getContents().put("itinerary", responseItinerary);
        }
        if(!responseEvent.isEmpty()){
            contentOrActivity.getContents().put("event", responseEvent);
        }
        if(!responseContest.isEmpty()){
            contentOrActivity.getContents().put("contest", responseContest);
        }
        if(contentOrActivity.getContents().isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Al momento non sono presenti contenuti o attività inerenti al comune");
        }

        return ResponseEntity.ok(contentOrActivity);
    }

    @PutMapping("/visited/municipality")
    @Operation(summary = "Visita un comune")
    public ResponseEntity<String> visitedMunicipality(@RequestParam String newMunicipality){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();


        if(role.equals(Role.ADMIN.name()) || role.equals(Role.MUNICIPALITY_MANAGER.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        this.userService.visitMunicipality(newMunicipality.toUpperCase(Locale.ROOT), idUser);
        return ResponseEntity.ok("Visita il comune eseguita con successo");
    }

    @GetMapping("/view/all/review/single/POI")
    @Operation(summary = "Visualizza recensioni",
            description = "Visualizza tutte le recensioni di uno specifico punto di interesse. " +
                    "Usa uno degli ID disponibili da /view/all/content/by/municipality.")
    public ResponseEntity<ReviewPOI> reviewSinglePoint(@RequestParam long idPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        InterestPoint interestPoint = this.interestPointService.GetSinglePoint(idPoint);

        List<ReviewPOIResponse> reviews = this.reviewService.GetReviewSinglePoint(interestPoint);
        ReviewPOI response = new ReviewPOI();
        response.setNamePOI(interestPoint.getTitle());
        response.setReference(interestPoint.getReference().getName());
        response.setReviews(reviews);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all/contests/I/participated/to")
    @Operation(summary = "Visualizza tutti i contest a cui si è partecipato")
    public  ResponseEntity<List<ContestProgress>> contestIPartecipated(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        User user = this.userService.getUser(idUser);
        List<ContestProgress> response = this.contestService.getContestPartecipated(user);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/account")
    @Operation(summary = "Elimina il proprio account e tutti i contenuti associati")
    public ResponseEntity<String> deleteAccount(){
        //TODO chiamare il metodo del servizio dell'utente e passare l'id dell'utente
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(!municipality.equals(visitedMunicipality) || role.equals(Role.ADMIN.name())
                || role.equals(Role.MUNICIPALITY_MANAGER.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        this.userService.deleteAccount(idUser);

        return ResponseEntity.ok("Account eliminato con successo");
    }
}
