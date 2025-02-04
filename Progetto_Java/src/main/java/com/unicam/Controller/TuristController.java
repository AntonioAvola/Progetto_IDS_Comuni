package com.unicam.Controller;

import com.unicam.Entity.CommandPattern.ReviewCommand;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.*;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

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
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MediaService mediaService;

    @PutMapping("/add/to/favorite")
    @Operation(summary = "Aggiungi ai preferiti",
            description = "Aggiunge un punto di interesse, un itinerario o un evento ai tuoi preferiti. " +
                    "Usa uno degli ID disponibili da /view/all/content/by/municipality.")
    public ResponseEntity<String> AddToFavorite(
        @Parameter(description = "Tipo di contenuto",
            schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY", "EVENT"}))
        @RequestParam(defaultValue = "INTEREST POINT") String type,
        @RequestParam long idContent){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(municipality.equals(visitedMunicipality))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        if (type.equals("INTEREST POINT")) {
          this.interestPointService.addFavorite(idContent, idUser);
        }
        else if (type.equals("ITINERARY")) {
            this.itineraryService.addFavorite(idContent, idUser);
        }
        else {
            this.eventService.addFavorite(idContent, idUser);
        }
        return ResponseEntity.ok("Contenuto aggiunto con successo ai preferiti");
    }

    @PutMapping("/reported")
    @Operation(summary = "Segnala un contenuto",
            description = "Segnala un punto di interesse o un itinerario che ritineri non opportuno. " +
                    "Usa uno degli ID disponibili da /view/all/content/by/municipality.")
    public ResponseEntity <String> reportContent(
            @Parameter(description = "Tipo di contenuto",
                    schema = @Schema(type = "String", allowableValues = {"INTEREST POINT", "ITINERARY"}))
            @RequestParam(defaultValue = "INTEREST POINT") String type,
            @RequestParam long idContent) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(municipality.equals(visitedMunicipality))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        if(type.equals("INTEREST POINT")) {
            this.interestPointService.reportPOI(idContent);
            return ResponseEntity.ok("Punto di interesse segnalato con successo");
        }
        else {
            this.itineraryService.reportItinerary(idContent);
            return ResponseEntity.ok("Itinerario segnalato con successo");
        }
    }

    @PutMapping(value = "/add/review", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Aggiungi una recensione",
            description = "Aggiunge una recensione per uno specifico punto di interesse. " +
                    "Usa uno degli ID disponibili da /view/all/content/by/municipality.")
    public ResponseEntity<String> addReview(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam long referencePOI,
            @Validated @RequestParam(value = "fileUploaded", required = false)
            List<MultipartFile> fileUploaded) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = this.userService.getUser(idUser).getVisitedMunicipality();

        if(municipality.equals(visitedMunicipality))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        User author = this.userService.getUser(idUser);

        InterestPoint reference = this.interestPointService.getPointById(referencePOI);

        ReviewCommand review = new ReviewCommand(title, description, author, reference, fileUploaded, reviewService, mediaService);
        review.execute();

        return ResponseEntity.ok("Recensione aggiunta con successo");
    }
}
