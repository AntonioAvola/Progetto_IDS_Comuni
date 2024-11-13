package com.unicam.Controller;

import com.unicam.DTO.ContentDelete;
import com.unicam.DTO.ItineraryRequest;
import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.CommandPattern.InterestPointCommand;
import com.unicam.Entity.CommandPattern.ItineraryCommand;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Content.InterestPoint;
import com.unicam.Entity.Content.Itinerary;
import com.unicam.Entity.User;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.InterestPointService;
import com.unicam.Service.Content.ItineraryService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(name = "api/contributor")
public class ContributorController {
    @Autowired
    private InterestPointService interestPointService;
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ProxyOSM proxy;

    @PostMapping("Api/Contributor/addInterestPoint")
    public void AddInterestPoint(InterestPointRequest request) throws IOException {
        //TODO controlla autorizzazioni
        String address = request.getTitle();
        String currentMunicipality = URLEncoder.encode("castelfidardo", StandardCharsets.UTF_8.toString());
        address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );

        //TODO controlla ruolo
        InterestPointCommand InterestPoint = new InterestPointCommand(request, user, ContentStatus.PENDING, coordinates);
        InterestPoint.execute();
    }

    @PostMapping("Api/Contributor/addItinerary")
    public void AddItinerary(ItineraryRequest request) {
        //TODO controlla autorizzazioni
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );

        //TODO controlla lunghezza lista punti di interesse
        //TODO controlla presenza titolo
        //TODO controlla ruolo
        ItineraryCommand Itinerary = new ItineraryCommand(request, user, ContentStatus.PENDING);
        Itinerary.execute();
    }

    @DeleteMapping ("Api/Contributor/DeleteContent")
    public void DeleteContent(ContentDelete request) {
        //TODO controlla autorizzazioni
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );

        if (request.getType().equals("InterestPoint")){
            InterestPoint Point = this.interestPointService.getInterestPoint(request.getTitle());
            this.interestPointService.removeInterestPoint(Point);
        }
        else if (request.getType().equals("Itinerary")) {
            Itinerary itinerary = this.itineraryService.getItinerary(request.getTitle());
            this.itineraryService.removeItinerary(itinerary);
        }

    }
}
