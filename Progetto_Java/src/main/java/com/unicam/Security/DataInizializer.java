package com.unicam.Security;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Municipality;
import com.unicam.Entity.Role;
import com.unicam.Entity.User;
import com.unicam.Repository.Content.ContestRepository;
import com.unicam.Repository.Content.EventRepository;
import com.unicam.Repository.Content.InterestPointRepository;
import com.unicam.Repository.Content.ItineraryRepository;
import com.unicam.Repository.MunicipalityRepository;
import com.unicam.Repository.UserRepository;
import com.unicam.Service.MunicipalityService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
    private ItineraryRepository intineraryRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ProxyOSM proxy;

    private List<String> municipalityNames = new ArrayList<>(List.of("MILANO", "ROMA", "NAPOLI"));

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MunicipalityService municipalityService;
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
                i += 1;
            }
            CreateMunicipalityRequest(municipalityNames);
            System.out.println("----------------------------------------------------");
        }
    }

    private void CreateMunicipalityRequest(List<String> municipalityNames) throws Exception{
        for(String name : municipalityNames){
            MunicipalityDetails details = proxy.getDetails(name);
            Municipality municipality = new Municipality(name, "", details);
            if(name.equals("MILANO")){
                municipality.setStatus(ContentStatus.APPROVED);
            }
            municipalityRepository.save(municipality);
            System.out.println("Richiesta comune inserita!");
        }
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
        System.out.println("authorizedContributor creato con successo!");
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
        System.out.println("contributor creato con successo!");
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
        System.out.println("Animator creato con successo!");
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
        System.out.println("Curator creato con successo!");
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
        System.out.println("Manager creato con successo!");
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
