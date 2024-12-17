package com.unicam.Controller;

import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "api/turist")
public class TuristController {
    @Autowired
    private UserService userService;
    private InterestPointService interestPointService;
    private ItineraryService itineraryService;
    private EventService eventService;

    @PutMapping("AddToFavorite")
    public ResponseEntity <String> AddToFavorite(
        @Parameter(description = "Tipo di contenuto",
            schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY", "EVENT"}))
        @RequestParam(defaultValue = "INTEREST POINT") String type,
        @RequestParam long idContent){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        if (type.equals("INTEREST POINT")) {
          this.interestPointService.addFavorite(idUser, idContent);
        } else if (type.equals("ITINERARY")) {
            this.itineraryService.addFavorite(idUser, idContent);
        } else
            this.eventService.addFavorite(idUser, idContent);
        return ResponseEntity.ok("Operazione eseguita con successo");
    }
}
