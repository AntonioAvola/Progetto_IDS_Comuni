package com.unicam.Controller;

import com.unicam.DTO.Response.ContentOrActivityPending;
import com.unicam.DTO.Response.InterestPointResponse;
import com.unicam.DTO.Response.ItineraryResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.*;
import com.unicam.Service.UserService;
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

    @PutMapping("api/curator/approve/content")
    public ResponseEntity<String> approveContent(@RequestParam String type, @RequestParam long idContent){

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

        if(type.equals("interest point")){
            if(!this.interestPointService.approveOrRejectPoint(idContent, ContentStatus.APPROVED))
                throw new UnsupportedOperationException("Punto di interesse non trovato");
            return ResponseEntity.ok("Punto di interesse approvato con successo");
        }
        else if(type.equals("itinerary")){
            if(!this.itineraryService.approveOrRejectItinerary(idContent, ContentStatus.APPROVED))
                throw new UnsupportedOperationException("Itinerario non trovato");
            return ResponseEntity.ok("Itinerario approvato con successo");
        }
        else{
            throw new IllegalArgumentException("Azione non supportata. Assicurati di aver inserito bene la tipologia di contenuto da eliminare");
        }
    }

    @DeleteMapping("api/curator/reject/content")
    public ResponseEntity<String> rejectContent(@RequestParam String type, @RequestParam long idContent) {

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

        if (type.equals("interest point")) {
            if (!this.interestPointService.approveOrRejectPoint(idContent, ContentStatus.REJECTED))
                throw new UnsupportedOperationException("Punto di interesse non trovato");
            return ResponseEntity.ok("Punto di interesse rifiutato con successo");
        } else if (type.equals("itinerary")) {
            if (!this.itineraryService.approveOrRejectItinerary(idContent, ContentStatus.REJECTED))
                throw new UnsupportedOperationException("Itinerario non trovato");
            return ResponseEntity.ok("Itinerario rifiutato con successo");
        } else {
            throw new IllegalArgumentException("Azione non supportata. Assicurati di aver inserito bene la tipologia di contenuto da eliminare");
        }
    }
}
