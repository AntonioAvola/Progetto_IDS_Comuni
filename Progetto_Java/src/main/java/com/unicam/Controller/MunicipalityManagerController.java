package com.unicam.Controller;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.DTO.Response.*;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Municipality;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.MunicipalityService;
import com.unicam.Service.PromotionService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(name = "api/MunicipalityManager")
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


    @PostMapping("api/municipalityManager/addMunicipality")
    public void addMunicipality(@RequestParam String description) throws IOException {

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

        if(this.municipalityService.exists(municipality))
            throw new UnsupportedOperationException("Il comune è già presente o è già stata inviata una richiesta");

        MunicipalityDetails details = proxyOSM.getDetails(municipality);
        Municipality municipalityToBeAdd = new Municipality(municipality, description, details);
        this.municipalityService.addMunicipality(municipalityToBeAdd);
    }

    @GetMapping("api/municipalityManager/view/all/activity/pending")
    @Operation(summary = "Visualizza attività in attesa",
            description = "Restituisce una lista di attività in attesa con i relativi ID.")
    public ResponseEntity<ContentOrActivityPending> getActivityPending(){

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

        List<EventResponse> eventPending = this.eventService.getEvent(municipality, ContentStatus.PENDING);
        List<ContestResponse> contestPending = this.contestService.getContest(municipality, ContentStatus.PENDING);
        ContentOrActivityPending pending = new ContentOrActivityPending();
        pending.getContents().put("event", eventPending);
        pending.getContents().put("contest", contestPending);

        return ResponseEntity.ok(pending);
    }

    @PutMapping("api/municipalityManager/approve/or/reject/activity")
    @Operation(summary = "Approva o rifiuta un'attività",
            description = "Approva o rifiuta un'attività in attesa. Usa uno degli ID disponibili da /getActivityPending.")
    public ResponseEntity<String> approveOrRejectContent(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"EVENT", "CONTEST"}))
            @RequestParam(defaultValue = "EVENT") String type,
            @RequestParam long idContent,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

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

        if(type.equals("EVENT")){
            if(!this.eventService.checkMunicipality(idContent, municipality))
                throw new IllegalArgumentException("Evento non appartenente al comune di " + municipality);
            this.eventService.approveOrRejectPoint(idContent, status);
        }
        else {
            if(!this.contestService.checkMunicipality(idContent, municipality))
                throw new IllegalArgumentException("Contest non appartenente al comune di " + municipality);
            this.contestService.approveOrRejectItinerary(idContent, status);
        }
        return ResponseEntity.ok("Operazione eseguita con successo");
    }

    @PutMapping("api/municipalityManager/approve/or/reject/promotion")
    public ResponseEntity<String> approveOrRejectPromotion(
            @RequestParam long idPromotion,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        if(status.equals(ContentStatus.APPROVED)){
            this.userService.updateRole(idPromotion);
        }
        this.promotionService.removePromotionRequest(idPromotion);
        return ResponseEntity.ok("Operazione eseguita con successo!");
    }

    @GetMapping("/getPromotionRequests")
    public ResponseEntity<List<PromotionResponse>> getPromotionRequests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        List<PromotionResponse> responses = this.promotionService.getAllPromotionRequests(municipality);

        return ResponseEntity.ok(responses);
    }
}
