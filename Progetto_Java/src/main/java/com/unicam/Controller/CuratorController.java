package com.unicam.Controller;

import com.unicam.DTO.Response.ContentOrActivity;
import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.DTO.Response.ItineraryResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.*;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/curator")
public class CuratorController {

    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private UserService userService;

    @GetMapping("/view/all/content/pending")
    @Operation(summary = "Visualizza contenuti in attesa",
            description = "Restituisce una lista di contenuti in attesa con i relativi ID.")
    public ResponseEntity<ContentOrActivity> getContentPending(){

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

        List<InterestPointResponse> interestPointPending = this.interestPointService.getPoint(municipality, ContentStatus.PENDING);
        List<ItineraryResponse> itineraryPending = this.itineraryService.getItinerary(municipality, ContentStatus.PENDING);
        ContentOrActivity pending = new ContentOrActivity();
        pending.getContents().put("interest point", interestPointPending);
        pending.getContents().put("itineray", itineraryPending);

        return ResponseEntity.ok(pending);
    }

    @PutMapping("/approve/or/reject/content/or/report")
    @Operation(summary = "Approva o rifiuta un contenuto o segnalazione",
            description = "Approva o rifiuta un contenuto in attesa o segnalazione. Usa uno degli ID disponibili da /getContentPending.")
    public ResponseEntity<String> ValidatePendingOrReported(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY"}))
            @RequestParam(defaultValue = "INTEREST POINT") String type,
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

        if(type.equals("INTEREST POINT")){
            if(!this.interestPointService.checkMunicipality(idContent, municipality))
                throw new IllegalArgumentException("Punto di interesse non appartenente al comune di " + municipality);
            this.interestPointService.approveOrRejectPoint(idContent, status);
        }
        else {
            if(!this.itineraryService.checkMunicipality(idContent, municipality))
                throw new IllegalArgumentException("Itinerario non appartenente al comune di " + municipality);
            this.itineraryService.approveOrRejectItinerary(idContent, status);
        }
        return ResponseEntity.ok("Operazione eseguita con successo");
    }

    @GetMapping("/view/all/reported/contents")
    public ResponseEntity <ContentOrActivity> ViewAllReportedContent(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        List <InterestPointResponse> Point = this.interestPointService.getPoint(municipality, ContentStatus.REPORTED);
        List <ItineraryResponse> Itinerary = this.itineraryService.getItinerary(municipality, ContentStatus.REPORTED);
        ContentOrActivity Response = new ContentOrActivity();
        Response.getContents().put("InterestPoint", Point);
        Response.getContents().put("Itinerary", Itinerary);
        return ResponseEntity.ok(Response);
    }
}
