package com.unicam.Controller;

import com.unicam.DTO.Response.ContentOrActivityPending;
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
@RequestMapping(name = "Api/Curator")
public class CuratorController {

    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private UserService userService;

    @GetMapping("api/curator/view/all/content/pending")
    @Operation(summary = "Visualizza contenuti pendenti",
            description = "Restituisce una lista di contenuti in attesa con i relativi ID.")
    public ResponseEntity<ContentOrActivityPending> getContentPending(){

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
        ContentOrActivityPending pending = new ContentOrActivityPending();
        pending.getContents().put("interest point", interestPointPending);
        pending.getContents().put("itineray", itineraryPending);

        return ResponseEntity.ok(pending);
    }

    @PutMapping("api/curator/approve/or/reject/content")
    @Operation(summary = "Approva un contenuto",
            description = "Approva un contenuto in attesa. Usa uno degli ID disponibili da /getContentPending.")
    public ResponseEntity<String> approveOrRejectContent(
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
}
