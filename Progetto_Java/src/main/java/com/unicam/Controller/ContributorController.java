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
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void AddInterestPoint(InterestPointRequest request) throws IOException {
        //TODO controlla autorizzazioni
        String address = request.getTitle();
        String currentMunicipality = URLEncoder.encode("Roma", StandardCharsets.UTF_8.toString());
        address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );
        this.userService.addAccount(user);

        //TODO controlla ruolo
        InterestPointCommand InterestPoint = new InterestPointCommand(request, user, interestPointService, geoPointService, ContentStatus.PENDING, coordinates);
        InterestPoint.execute();
    }

    @PostMapping("Api/Contributor/addItinerary")
    public void AddItinerary(ItineraryRequest request) {
        //TODO controlla autorizzazioni
        User user = new User ("Emma", "emma", "Tolentino", "emma@gmail.com", "IDS" );
        this.userService.addAccount(user);

        //TODO controlla lunghezza lista punti di interesse
        //TODO controlla presenza titolo
        //TODO controlla ruolo
        ItineraryCommand Itinerary = new ItineraryCommand(request, itineraryService, interestPointService, user, ContentStatus.PENDING);
        Itinerary.execute();
    }

    @DeleteMapping ("Api/Contributor/DeleteContent")
    public void DeleteContent(ContentDelete request) {
        //TODO controlla autorizzazioni
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );

        if (request.getType().equals("interest point")){
            InterestPoint Point = this.interestPointService.getInterestPoint(request.getTitle());
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
