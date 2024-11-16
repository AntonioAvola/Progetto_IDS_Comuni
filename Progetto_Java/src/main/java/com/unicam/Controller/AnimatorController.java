package com.unicam.Controller;

import com.unicam.DTO.ContentDelete;
import com.unicam.DTO.Request.ContestRequest;
import com.unicam.DTO.Request.EventRequest;
import com.unicam.Entity.CommandPattern.ContestCommand;
import com.unicam.Entity.CommandPattern.EventCommand;
import com.unicam.Entity.Content.Contest;
import com.unicam.Entity.Content.Event;
import com.unicam.Entity.User;
import com.unicam.Service.Content.ContestService;
import com.unicam.Service.Content.EventService;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "Api/Animator")
public class AnimatorController {

    @Autowired
    private ContestService contestService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GeoPointService geoPointService;
    @Autowired
    private UserService userService;

    @PostMapping("Api/Animator/AddEvent")
    public void Addevent(EventRequest request){
        //TODO controllo autorizzazioni
        User user = new User ("Maria", "mary", "Tolentino", "mary@gmail.com", "IDS" );
        this.userService.addAccount(user);

        //TODO controllo presenza GeoPoint
        //TODO controllo presenza title
        //TODO controllo su inizio e fine
        EventCommand event = new EventCommand(request, eventService, geoPointService, user);
        event.execute();
    }

    @PostMapping("Api/Animator/AddContest")
    public void AddContest(ContestRequest request){
        //TODO controllo autorizzazioni
        User user = new User ("lucia", "lucy", "Tolentino", "lucy@gmail.com", "IDS" );
        this.userService.addAccount(user);

        //TODO controllo su inizio e fine
        //TODO controllo presenza title
        ContestCommand contest = new ContestCommand(request, contestService, user);
        contest.execute();
    }

    @DeleteMapping("Api/Animator/DeleteActivity")
    public void DeleteActivity(ContentDelete request){
        //TODO controllo autorizzazioni
        User user = new User ("Sofia", "sofia", "Tolentino", "sofia@gmail.com", "IDS" );

        if(request.getType().equals("event")){
            Event event = this.eventService.getEvent(request.getTitle(), user.getId());
            this.eventService.removeEvent(event);
        }
        else if(request.getType().equals("contest")){
            Contest contest = this.contestService.getContest(request.getTitle(), user.getId());
            this.contestService.removeContest(contest);
        }
        else
            throw new IllegalArgumentException("Azione non supportata. Assicurati di aver inserito bene la tipologia di attività da eliminare");
    }
}
