package com.unicam.Controller;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.DTO.Response.*;
import com.unicam.Entity.CommandPattern.MunicipalityCommand;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.MunicipalityService;
import com.unicam.Service.PromotionService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/municipalityManager")
public class MunicipalityManagerController {

    @Autowired
    private MunicipalityService municipalityService;
    @Autowired
    private ProxyOSM proxyOSM;
    @Autowired
    private EventService eventService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private UserService userService;


    @PostMapping("/add/municipality")
    public void addMunicipality(@RequestParam String description) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        if(this.municipalityService.exists(municipality))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Il comune è già presente o è già stata inviata una richiesta");

        MunicipalityDetails details = proxyOSM.getDetails(municipality);
        MunicipalityCommand municipalityAdd = new MunicipalityCommand(municipalityService, municipality, description, details);
        municipalityAdd.execute();
    }

    @GetMapping("/view/all/activity/pending")
    @Operation(summary = "Visualizza attività in attesa",
            description = "Restituisce una lista di attività in attesa con i relativi ID.")
    public ResponseEntity<ContentOrActivity> getActivityPending(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        List<EventResponse> eventPending = this.eventService.getEvent(municipality, ContentStatus.PENDING);
        List<ContestResponse> contestPending = this.contestService.getContest(municipality, ContentStatus.PENDING);
        ContentOrActivity response = new ContentOrActivity();
        if(!eventPending.isEmpty()){
            response.getContents().put("event", eventPending);
        }
        if(!contestPending.isEmpty()){
            response.getContents().put("contest", contestPending);
        }
        if(response.getContents().isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Al momento non sono presenti contenuti segnalati");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve/or/reject/activity")
    @Operation(summary = "Approva o rifiuta un'attività",
            description = "Approva o rifiuta un'attività in attesa. Usa uno degli ID disponibili da /getActivityPending.")
    public ResponseEntity<String> approveOrRejectActivity(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"EVENT", "CONTEST"}))
            @RequestParam(defaultValue = "EVENT") String type,
            @RequestParam long idActivity,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        if(type.equals("EVENT")){
            this.eventService.validateEvent(idActivity, status);
            if(status.equals(ContentStatus.APPROVED)){
                return ResponseEntity.ok("Evento approvato con successo");
            }
            else{
                return ResponseEntity.ok("Evento rifiutato");
            }
        }
        else {
            this.contestService.validateContest(idActivity, status);
            if(status.equals(ContentStatus.APPROVED)){
                return ResponseEntity.ok("Contest approvato con successo");
            }
            else{
                return ResponseEntity.ok("Contest rifiutato");
            }
        }
    }

    @PutMapping("/approve/or/reject/role/promotion")
    public ResponseEntity<String> approveOrRejectPromotion(
            @RequestParam long idPromotion,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        String response = "Promozione rifiutata";

        if(status.equals(ContentStatus.APPROVED)){
            this.userService.updateRole(idPromotion);
            response = "Promozione accettata";
        }
        this.promotionService.removePromotionRequest(idPromotion);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/all/promotion/requests")
    public ResponseEntity<List<PromotionResponse>> getPromotionRequests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        List<PromotionResponse> responses = this.promotionService.getAllPromotionRequests(municipality);
        if(responses.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Al momento non sono presenti richieste di promozione del ruolo");
        }

        return ResponseEntity.ok(responses);
    }
}
