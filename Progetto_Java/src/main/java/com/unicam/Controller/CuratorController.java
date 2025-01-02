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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        List<InterestPointResponse> interestPointPending = this.interestPointService.getPoint(municipality, ContentStatus.PENDING);
        List<ItineraryResponse> itineraryPending = this.itineraryService.getItinerary(municipality, ContentStatus.PENDING);
        ContentOrActivity response = new ContentOrActivity();
        if(!interestPointPending.isEmpty()){
            response.getContents().put("interest point", interestPointPending);
        }
        if(!itineraryPending.isEmpty()){
            response.getContents().put("itineray", itineraryPending);
        }
        if(response.getContents().isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Al momento non sono presenti contenuti in attesa di validazione");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve/or/reject/content")
    @Operation(summary = "Validazione contenuto",
            description = "Approva o rifiuta un contenuto in attesa. " +
                    "Usa uno degli ID disponibili da /view/all/content/pending.")
    public ResponseEntity<String> ValidatePending(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY"}))
            @RequestParam(defaultValue = "INTEREST POINT") String type,
            @RequestParam long idContent,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

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

        if(type.equals("INTEREST POINT")){
            this.interestPointService.approveOrRejectPoint(idContent, status);
            if(status.equals(ContentStatus.APPROVED)){
                return ResponseEntity.ok("Punto di interesse approvato con successo");
            }
            else{
                return ResponseEntity.ok("Punto di interesse rifiutato");
            }
        }
        else {
            this.itineraryService.approveOrRejectItinerary(idContent, status);
            if(status.equals(ContentStatus.APPROVED)){
                return ResponseEntity.ok("Itinerario approvato con successo");
            }
            else{
                return ResponseEntity.ok("Itinerario rifiutato");
            }
        }
    }


    @GetMapping("/view/all/reported/contents")
    @Operation(summary = "Visualizza contenuti segnalati",
            description = "Restituisce la lista di contenuti segnalati con i relativi ID.")
    public ResponseEntity <ContentOrActivity> ViewAllReportedContent(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        List <InterestPointResponse> points = this.interestPointService.getPoint(municipality, ContentStatus.REPORTED);
        List <ItineraryResponse> itineraries = this.itineraryService.getItinerary(municipality, ContentStatus.REPORTED);
        ContentOrActivity response = new ContentOrActivity();
        if(!points.isEmpty()){
            response.getContents().put("InterestPoint", points);
        }
        if(!itineraries.isEmpty()){
            response.getContents().put("Itinerary", itineraries);
        }
        if(response.getContents().isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Al momento non sono presenti contenuti segnalati");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve/or/reject/report")
    @Operation(summary = "Validazione contenuto segnalato",
            description = "Approva o rifiuta un contenuto segnalato. " +
                    "Usa uno degli ID disponibili da /view/all/reported/contents.")
    public ResponseEntity<String> ValidateReported(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY"}))
            @RequestParam(defaultValue = "INTEREST POINT") String type,
            @RequestParam long idContent,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

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

        if(type.equals("INTEREST POINT")){
            this.interestPointService.approveOrRejectPoint(idContent, status);
            if(status.equals(ContentStatus.APPROVED)){
                return ResponseEntity.ok("Punto di interesse approvato con successo");
            }
            else{
                return ResponseEntity.ok("Punto di interesse rifiutato");
            }
        }
        else {
            this.itineraryService.approveOrRejectItinerary(idContent, status);
            if(status.equals(ContentStatus.APPROVED)){
                return ResponseEntity.ok("Itinerario approvato con successo");
            }
            else{
                return ResponseEntity.ok("Itinerario rifiutato");
            }
        }
    }
}
