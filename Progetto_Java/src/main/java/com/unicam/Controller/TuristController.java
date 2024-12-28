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
@RequestMapping("api/turist")
public class TuristController {
    @Autowired
    private UserService userService;
    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private EventService eventService;

    @PutMapping("/add/to/favorite")
    public ResponseEntity <String> AddToFavorite(
        @Parameter(description = "Tipo di contenuto",
            schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY", "EVENT"}))
        @RequestParam(defaultValue = "INTEREST POINT") String type,
        @RequestParam long idContent){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        if (type.equals("INTEREST POINT")) {
          this.interestPointService.addFavorite(idContent, idUser);
        } else if (type.equals("ITINERARY")) {
            this.itineraryService.addFavorite(idContent, idUser);
        } else
            this.eventService.addFavorite(idContent, idUser);
        return ResponseEntity.ok("Operazione eseguita con successo");
    }
}
