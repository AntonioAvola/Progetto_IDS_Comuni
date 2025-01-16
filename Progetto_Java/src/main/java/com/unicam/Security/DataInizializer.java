package com.unicam.Security;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.*;
import com.unicam.Entity.Content.*;
import com.unicam.Repository.Content.*;
import com.unicam.Repository.MunicipalityRepository;
import com.unicam.Repository.PromotionRepository;
import com.unicam.Repository.UserRepository;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInizializer  implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MunicipalityRepository MunicipalityRepository;

    @Autowired
    private InterestPointRepository interestPointRepository;
    @Autowired
    private GeoPointRepository geoPointRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProxyOSM proxy;

    private List<String> municipalityNames = new ArrayList<>(List.of("MILANO", "ROMA", "NAPOLI", "FIRENZE"));
    private List<String> poiMilan = new ArrayList<>(List.of("TEATRO ALLA SCALA", "CASTELLO SFORZESCO", "ARCO DELLA PACE", "PALAZZO REALE", "BASILICA DI SANT'AMBROGIO", "TORRE VELASCA", "ARCHI DI PORTA NUOVA"));
    private List<String> poiRome = new ArrayList<>(List.of("COLOSSEO", "PANTHEON", "FONTANA DI TREVI", "PIAZZA DI SPAGNA", "ARCO DI TITO", "OBELISCO"));

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            System.out.println("----------------------------------------------------");
            CreateSuperAdmin();
            System.out.println("----------------------------------------------------");
            int i = 1;
            for(String name : municipalityNames){
                CreateMunicipalityManager(name, i);
                CreateCurator(name, i);
                CreateAnimator(name, i);
                CreateContributor(name, i);
                CreateAuthorizedContributor(name, i);
                System.out.println("----------------------------------------------------");
                if(!name.equals("FIRENZE")){
                    CreateMunicipalityRequest(name);
                }
                System.out.println("----------------------------------------------------");
                if(name.equals("MILANO") || name.equals("ROMA")){
                    CreatePOI(name);
                    System.out.println("----------------------------------------------------");
                    CreateItinerary(name);
                    System.out.println("----------------------------------------------------");
                    CreateEvent(name);
                    System.out.println("----------------------------------------------------");
                    CreateContest(name);
                    System.out.println("----------------------------------------------------");
                }
                i += 1;
            }
            createPromotionRequest();
            System.out.println("----------------------------------------------------");
            createReviewMilan();
        }
    }

    private void createReviewMilan() {
        User user = this.userRepository.findUserById(9);
        InterestPoint point = this.interestPointRepository.findById(2);

        Review review1 = new Review();
        review1.setTitle("passeggiata piacevole");
        review1.setDescription("Passeggiata pomeridiana insieme alla mia famiglia. Le mie bambine hanno trovato il luogo molto di loro gradimento");
        review1.setMunicipality("MILANO");
        review1.setStatus(ContentStatus.APPROVED);
        review1.setAuthor(user);
        review1.setReference(point);
        this.reviewRepository.save(review1);
        System.out.println("MILANO" + "; " + point.getTitle() + ": recensione aggiunta con successo");

        Review review2 = new Review();
        review2.setTitle("bel paesaggio");
        review2.setDescription("bellissimi monumenti. Ben collocati nello spazio");
        review2.setMunicipality("MILANO");
        review2.setStatus(ContentStatus.APPROVED);
        review2.setAuthor(user);
        review2.setReference(point);
        this.reviewRepository.save(review2);
        System.out.println("MILANO" + "; " + point.getTitle() + ": recensione aggiunta con successo");
    }

    private void createPromotionRequest() {
        User user = this.userRepository.findByUsername("animator1");
        RolePromotion promotion = new RolePromotion(user, Role.CURATOR, user.getMunicipality(), "");

        this.promotionRepository.save(promotion);
        System.out.println(user.getMunicipality() + "; " + user.getUsername() + ": Richiesta promozione ruolo con successo");

        User user2 = this.userRepository.findByUsername("curator1");
        RolePromotion promotion2 = new RolePromotion(user2, Role.ANIMATOR, user.getMunicipality(), "");

        this.promotionRepository.save(promotion2);
        System.out.println(user2.getMunicipality() + "; " + user2.getUsername() + ": Richiesta promozione ruolo con successo");
    }

    private void CreateContest(String name) {
        List<User> users = this.userRepository.findAll();
        User animator;
        if(name.equals("ROMA")){
            animator = this.userRepository.findUserById(9);
        }
        else{
            animator = this.userRepository.findUserById(4);
        }

        Contest contest1 = new Contest();
        contest1.setTitle("ballo");
        contest1.setAuthor(animator);
        contest1.setDescription("gara di ballo a coppie. Basta la registrazione di un componente della coppia");
        contest1.setReward("€200");
        contest1.setMunicipality(name);
        contest1.setStatus(ContentStatus.APPROVED);
        contest1.setDuration(new Time(LocalDateTime.parse("2024-06-29T12:00:00"), LocalDateTime.parse("2024-06-29T21:00:00")));
        contest1.setParticipants(List.of(users.get(3), users.get(5)));
        contest1.setActivityStatus(ActivityStatus.FINISHED);
        this.contestRepository.save(contest1);
        System.out.println(name + ": contest " + contest1.getTitle() + " aggiunto con successo");

        Contest contest2 = new Contest();
        contest2.setTitle("pittura");
        contest2.setAuthor(animator);
        contest2.setDescription("pittura di un paesaggio");
        contest2.setReward("€100");
        contest2.setMunicipality(name);
        contest2.setStatus(ContentStatus.APPROVED);
        contest2.setDuration(new Time(LocalDateTime.parse("2025-03-12T12:00:00"), LocalDateTime.parse("2025-03-13T21:00:00")));
        contest2.setActivityStatus(ActivityStatus.WAITING);
        this.contestRepository.save(contest2);
        System.out.println(name + ": contest " + contest2.getTitle() + " aggiunto con successo");

        Contest contest3 = new Contest();
        contest3.setTitle("cucina");
        contest3.setAuthor(animator);
        contest3.setDescription("gara di cucina. Utensili messi a disposizione dal comune");
        contest3.setReward("€200");
        contest3.setMunicipality(name);
        contest3.setStatus(ContentStatus.PENDING);
        contest3.setDuration(new Time(LocalDateTime.parse("2025-04-10T12:00:00"), LocalDateTime.parse("2025-04-10T21:00:00")));
        this.contestRepository.save(contest3);
        System.out.println(name + ": contest " + contest3.getTitle() + " aggiunta proposta di attività");

        Contest contest4 = new Contest();
        contest4.setTitle("canto");
        contest4.setAuthor(animator);
        contest4.setDescription("gara di canto");
        contest4.setReward("€200");
        contest4.setMunicipality(name);
        contest4.setStatus(ContentStatus.PENDING);
        contest4.setDuration(new Time(LocalDateTime.parse("2025-05-01T12:00:00"), LocalDateTime.parse("2025-05-01T21:00:00")));
        this.contestRepository.save(contest4);
        System.out.println(name + ": contest " + contest4.getTitle() + " aggiunta proposta di attività");
    }

    private void CreateEvent(String name){
        List<InterestPoint> pois = this.interestPointRepository.findByMunicipalityAndStatus(name, ContentStatus.APPROVED);
        pois.addAll(this.interestPointRepository.findByMunicipalityAndStatus(name, ContentStatus.REPORTED));
        User animator;
        if(name.equals("ROMA")){
            animator = this.userRepository.findUserById(9);
        }
        else{
            animator = this.userRepository.findUserById(4);
        }

        Event event1 = new Event();
        event1.setTitle("sagra pappardelle");
        event1.setDescription("pappardelle al cinghiale, all'anatra, al ragù o con la panna e piselli");
        event1.setReference(pois.get(0).getReference());
        event1.setMunicipality(name);
        event1.setAuthor(animator);
        event1.setStatus(ContentStatus.APPROVED);
        event1.setDuration(new Time(LocalDateTime.parse("2024-06-21T12:00:00"), LocalDateTime.parse("2024-06-28T15:00:00")));
        event1.setActivityStatus(ActivityStatus.FINISHED);
        this.eventRepository.save(event1);
        System.out.println(name + ": evento " + event1.getTitle() + " aggiunto con successo");

        Event event2 = new Event();
        event2.setTitle("sagra pizza e birra");
        event2.setDescription("pizza, patatine, birra, coca-cola, acqua");
        event2.setReference(pois.get(2).getReference());
        event2.setMunicipality(name);
        event2.setAuthor(animator);
        event2.setStatus(ContentStatus.APPROVED);
        event2.setDuration(new Time(LocalDateTime.parse("2025-02-26T12:00:00"), LocalDateTime.parse("2025-02-28T15:00:00")));
        event2.setActivityStatus(ActivityStatus.WAITING);
        this.eventRepository.save(event2);
        System.out.println(name + ": evento " + event2.getTitle() + " aggiunto con successo");

        Event event3 = new Event();
        event3.setTitle("festa di carnevale");
        event3.setDescription("pronti a travestirsi e divertirsi?!");
        event3.setReference(pois.get(1).getReference());
        event3.setMunicipality(name);
        event3.setAuthor(animator);
        event3.setStatus(ContentStatus.PENDING);
        event3.setDuration(new Time(LocalDateTime.parse("2025-03-03T16:00:00"), LocalDateTime.parse("2025-03-03T22:00:00")));
        this.eventRepository.save(event3);
        System.out.println(name + ": evento " + event3.getTitle() + " aggiunta proposta di attività");

        Event event4 = new Event();
        event4.setTitle("Pasqua");
        event4.setDescription("Preparatevi tutti per una bellissima caccia alle uova di pasqua nascoste per tutto il comune. Punto di inizio quello segnalato");
        event4.setReference(pois.get(3).getReference());
        event4.setMunicipality(name);
        event4.setAuthor(animator);
        event4.setStatus(ContentStatus.PENDING);
        event4.setDuration(new Time(LocalDateTime.parse("2025-04-20T14:00:00"), LocalDateTime.parse("2025-04-20T18:00:00")));
        this.eventRepository.save(event4);
        System.out.println(name + ": evento " + event4.getTitle() + " aggiunta proposta di attività");
    }
    
    private void CreateItinerary(String name) {
        List<InterestPoint> pois = this.interestPointRepository.findByMunicipalityAndStatus(name, ContentStatus.APPROVED);
        pois.addAll(this.interestPointRepository.findByMunicipalityAndStatus(name, ContentStatus.REPORTED));
        User author;
        User authorAuthorized;
        if(name.equals("ROMA")){
            author = this.userRepository.findUserById(10);
            authorAuthorized = this.userRepository.findUserById(11);
        }
        else{
            author = this.userRepository.findUserById(5);
            authorAuthorized = this.userRepository.findUserById(6);
        }

        Itinerary itinerary1 = new Itinerary();
        itinerary1.setTitle("corsa");
        itinerary1.setDescription("corsa breve per il comune");
        itinerary1.setAuthor(authorAuthorized);
        itinerary1.setStatus(ContentStatus.APPROVED);
        itinerary1.setPath(List.of(pois.get(0), pois.get(1), pois.get(2)));
        itinerary1.setMunicipality(name);
        this.itineraryRepository.save(itinerary1);
        System.out.println(name + ": itinerario " + itinerary1.getTitle() + " aggiunto con successo");

        Itinerary itinerary2 = new Itinerary();
        itinerary2.setTitle("passeggiata");
        itinerary2.setDescription("passeggiata breve per il comune");
        itinerary2.setAuthor(authorAuthorized);
        itinerary2.setStatus(ContentStatus.REPORTED);
        itinerary2.setPath(List.of(pois.get(0), pois.get(3)));
        itinerary2.setMunicipality(name);
        this.itineraryRepository.save(itinerary2);
        System.out.println(name + ": itinerario " + itinerary2.getTitle() + " aggiunto con successo");

        Itinerary itinerary3 = new Itinerary();
        itinerary3.setTitle("giro");
        itinerary3.setDescription("giro a piedi per il comune");
        itinerary3.setAuthor(authorAuthorized);
        itinerary3.setStatus(ContentStatus.REPORTED);
        itinerary3.setPath(List.of(pois.get(1), pois.get(2), pois.get(3)));
        itinerary3.setMunicipality(name);
        this.itineraryRepository.save(itinerary3);
        System.out.println(name + ": itinerario " + itinerary3.getTitle() + " aggiunto con successo");

        Itinerary itinerary4 = new Itinerary();
        itinerary4.setTitle("giro in bicicletta");
        itinerary4.setDescription("percorso con la bicicletta");
        itinerary4.setAuthor(author);
        itinerary4.setStatus(ContentStatus.PENDING);
        itinerary4.setPath(List.of(pois.get(0), pois.get(2)));
        itinerary4.setMunicipality(name);
        this.itineraryRepository.save(itinerary4);
        System.out.println(name + ": itinerario " + itinerary4.getTitle() + " aggiunta richiesta di inserimento");

        Itinerary itinerary5 = new Itinerary();
        itinerary5.setTitle("scampagnata");
        itinerary5.setDescription("percorso sicuro");
        itinerary5.setAuthor(author);
        itinerary5.setStatus(ContentStatus.PENDING);
        itinerary5.setPath(List.of(pois.get(1), pois.get(2)));
        itinerary5.setMunicipality(name);
        this.itineraryRepository.save(itinerary5);
        System.out.println(name + ": itinerario " + itinerary5.getTitle() + " aggiunta richiesta di inserimento");
    }

    private void CreatePOI(String name) throws IOException {
        if(name.equals("ROMA")){
            CreatePOIforCity("ROMA", poiRome);
        }
        else{
            CreatePOIforCity("MILANO", poiMilan);
        }
    }

    private void CreatePOIforCity(String city, List<String> monumentCity) throws IOException {
        int i = 0;
        for (String monument : monumentCity){
            String address = monument;
            String currentMunicipality = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
            address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            List<Double> coordinates = proxy.getCoordinates(address + "," + currentMunicipality);
            GeoPoint geoPoint = new GeoPoint(monument, city, coordinates.get(0), coordinates.get(1));
            this.geoPointRepository.save(geoPoint);

            InterestPointRequest request = new InterestPointRequest("monumento", "monumento storico", monument);
            InterestPoint interestPoint = new InterestPoint();
            interestPoint.setType(InterestPointType.HISTORICAL_MONUMENT);
            interestPoint.setReference(geoPoint);
            interestPoint.setTitle("monumento storico");
            interestPoint.setDescription("monumento storico della città");
            interestPoint.setMunicipality(city);
            interestPoint.setOpen(null);
            interestPoint.setClose(null);
            User author;
            ContentStatus status;
            if(i < 4){
                if(city.equals("ROMA")){
                    author = this.userRepository.findUserById(11);
                }
                else{
                    author = this.userRepository.findUserById(6);
                }
                if(i > 1){
                    status = ContentStatus.REPORTED;
                }else
                    status = ContentStatus.APPROVED;
            }
            else{
                if(city.equals("ROMA")){
                    author = this.userRepository.findUserById(10);
                }
                else{
                    author = this.userRepository.findUserById(5);
                }
                status = ContentStatus.PENDING;
            }
            interestPoint.setAuthor(author);
            interestPoint.setStatus(status);
            this.interestPointRepository.save(interestPoint);
            System.out.println(city + ": punto di interesse su " + monument + " aggiunto con successo");
            i++;
        }
    }

    private void CreateMunicipalityRequest(String municipalityName) throws Exception{
        MunicipalityDetails details = proxy.getDetails(municipalityName);
        Municipality municipality = new Municipality(municipalityName, "", details);
        if(municipalityName.equals("MILANO") || municipalityName.equals("ROMA")){
            municipality.setStatus(ContentStatus.APPROVED);
        }
        municipalityRepository.save(municipality);
        System.out.println(municipalityName + ": Richiesta comune inserita!");
    }

    private void CreateAuthorizedContributor(String name, int i) {
        User authorizedContributor = new User();
        authorizedContributor.setUsername("authorizedContributor" + i);
        authorizedContributor.setName("authorizedContributor" + i);
        authorizedContributor.setEmail("authorizedContributor"+i+"@authorizedContributor.com");
        authorizedContributor.setMunicipality(name);
        authorizedContributor.setVisitedMunicipality(name);
        authorizedContributor.setPassword(bCryptPasswordEncoder.encode("Authorized1!"));
        authorizedContributor.setRole(Role.AUTHORIZED_CONTRIBUTOR);

        userRepository.save(authorizedContributor);
        System.out.println(name + ": AuthorizedContributor creato con successo!");
    }

    private void CreateContributor(String name, int i) {
        User contributor = new User();
        contributor.setUsername("contributor" + i);
        contributor.setName("contributor" + i);
        contributor.setEmail("contributor"+i+"@contributor.com");
        contributor.setMunicipality(name);
        contributor.setVisitedMunicipality(name);
        contributor.setPassword(bCryptPasswordEncoder.encode("Contributor1!"));
        contributor.setRole(Role.CONTRIBUTOR);

        userRepository.save(contributor);
        System.out.println(name + ": Contributor creato con successo!");
    }

    private void CreateAnimator(String name, int i) {
        User animator = new User();
        animator.setUsername("animator" + i);
        animator.setName("animator" + i);
        animator.setEmail("animator"+i+"@animator.com");
        animator.setMunicipality(name);
        animator.setVisitedMunicipality(name);
        animator.setPassword(bCryptPasswordEncoder.encode("Animator1!"));
        animator.setRole(Role.ANIMATOR);

        userRepository.save(animator);
        System.out.println(name + ": Animator creato con successo!");
    }

    private void CreateCurator(String name, int i) {
        User curator = new User();
        curator.setUsername("curator" + i);
        curator.setName("curator" + i);
        curator.setEmail("curator"+i+"@curator.com");
        curator.setMunicipality(name);
        curator.setVisitedMunicipality(name);
        curator.setPassword(bCryptPasswordEncoder.encode("Curator1!"));
        curator.setRole(Role.CURATOR);

        userRepository.save(curator);
        System.out.println(name + ": Curator creato con successo!");
    }

    private void CreateMunicipalityManager(String name, int i) {
        User manager = new User();
        manager.setUsername("manager" + i);
        manager.setName("manager" + i);
        manager.setEmail("manager"+i+"@manager.com");
        manager.setMunicipality(name);
        manager.setVisitedMunicipality(name);
        manager.setPassword(bCryptPasswordEncoder.encode("Manager1!"));
        manager.setRole(Role.MUNICIPALITY_MANAGER);

        userRepository.save(manager);
        System.out.println(name + ": Manager creato con successo!");
    }


    public void CreateSuperAdmin(){
        User admin = new User();
        admin.setUsername("admin");
        admin.setName("admin");
        admin.setEmail("admin@admin.com");
        admin.setMunicipality("");
        admin.setVisitedMunicipality("");
        admin.setPassword(bCryptPasswordEncoder.encode("Admin1!"));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);
        System.out.println("Admin creato con successo!");
    }


}
