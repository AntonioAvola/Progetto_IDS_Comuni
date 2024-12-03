package com.unicam.Controller;

import com.unicam.DTO.MunicipalityDetails;
import com.unicam.Entity.Municipality;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Content.GeoPointService;
import com.unicam.Service.MunicipalityService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.rmi.UnexpectedException;

@RestController
@RequestMapping(name = "api/MunicipalityManager")
public class MunicipalityManagerController {

    @Autowired
    private MunicipalityService municipalityService;
    @Autowired
    private ProxyOSM proxyOSM;


    @PostMapping("api/addMunicipality")
    public void addMunicipality(@RequestParam String description) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        //TODO controllo comune:
        // se comune visitato è lo stesso del proprio comune allora proseguire;
        // altrimenti eccezione

        //TODO controllo ruolo

        if(this.municipalityService.exists(municipality))
            throw new UnsupportedOperationException("Il comune è già presente o è già stata inviata una richiesta");

        MunicipalityDetails details = proxyOSM.getDetails(municipality);
        Municipality municipalityToBeAdd = new Municipality(municipality, description, details);
        this.municipalityService.addMunicipality(municipalityToBeAdd);
    }
}
