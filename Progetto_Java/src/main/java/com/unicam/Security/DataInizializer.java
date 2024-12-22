package com.unicam.Security;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.DTO.Request.InterestPointRequest;
import com.unicam.Entity.Content.*;
import com.unicam.Entity.Municipality;
import com.unicam.Entity.Role;
import com.unicam.Entity.Time;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.*;
import com.unicam.Repository.MunicipalityRepository;
import com.unicam.Repository.UserRepository;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
    private EventRepository eventRepository;

    @Autowired
    private ContestRepository contestRepository;

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
                CreateMunicipalityRequest(name);
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
        }
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
