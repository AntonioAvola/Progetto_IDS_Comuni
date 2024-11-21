package com.unicam.Controller;

import com.unicam.DTO.ContentDelete;
import com.unicam.DTO.Request.ItineraryRequest;
import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.CommandPattern.InterestPointCommand;
import com.unicam.Entity.CommandPattern.ItineraryCommand;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.User;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(name = "api/contributor")
public class ContributorController {
    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private GeoPointService geoPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProxyOSM proxy;

    @PostMapping("Api/Contributor/addInterestPoint")
    public ResponseEntity<String> AddInterestPoint(InterestPointRequest request) throws IOException {
        //TODO controlla autorizzazioni

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();


        String address = request.getTitle();
        String currentMunicipality = URLEncoder.encode("Roma", StandardCharsets.UTF_8.toString());
        address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );
        this.userService.addAccount(user);

        //TODO controlla se esiste già il punto geolocalizzato, se esiste, lancia l'eccezione
//        if(this.geoPointService.checkGeoPointAlreadyExists(request.getReference()))
//            throw new UnsupportedOperationException("Esiste già un punto di interesse per questo determinato punto geolocalizzato");

        //TODO controlla ruolo
        InterestPointCommand InterestPoint = new InterestPointCommand(request, user, interestPointService, geoPointService, ContentStatus.PENDING, coordinates);
        InterestPoint.execute();

        return ResponseEntity.ok("Punto di interesse aggiunto con successo");
    }

    @PostMapping("Api/Contributor/addItinerary")
    public void AddItinerary(ItineraryRequest request) {
        //TODO controlla autorizzazioni
        User user = new User ("Emma", "emma", "Tolentino", "emma@gmail.com", "IDS" );
        this.userService.addAccount(user);

        //TODO controlla lunghezza lista punti di interesse, se non ha lunghezza minima 2 o ci sono punti di interesse ripetuti (true), lancia l'eccezione
//        if(this.itineraryService.checkPathLength(request.getPath()))
//            throw new IllegalArgumentException("Nella lista di punti di interesse sono presenti punti duplicati o meno di due punti di interesse");
//        //TODO controlla presenza titolo, se il titolo è già presente (true), lancia l'eccezione
//        if(this.itineraryService.checkTitle(request.getTitle() /*aggiungere anche il comune*/))
//            throw new IllegalArgumentException("Esiste già un itinerario con questo titolo. Inserire un titolo differente");

        //TODO controlla ruolo

        ItineraryCommand Itinerary = new ItineraryCommand(request, itineraryService, interestPointService, user, ContentStatus.PENDING);
        Itinerary.execute();
    }

    @DeleteMapping ("Api/Contributor/DeleteContent")
    public void DeleteContent(ContentDelete request) {
        //TODO controlla autorizzazioni

        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );

        if (request.getType().equals("interest point")){
            InterestPoint Point = this.interestPointService.getInterestPoint(request.getTitle() /*passare anche l'id dell'utente che richiede l'eliminazione del contenuto*/);
            this.interestPointService.removeInterestPoint(Point);
        }
        else if (request.getType().equals("itinerary")) {
            Itinerary itinerary = this.itineraryService.getItinerary(request.getTitle());
            this.itineraryService.removeItinerary(itinerary);
        }
        else
            throw new IllegalArgumentException("Azione non supportata. Assicurati di aver inserito bene la tipologia di contenuto da eliminare");
    }
}
