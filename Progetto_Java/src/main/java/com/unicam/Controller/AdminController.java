package com.unicam.Controller;

import com.unicam.DTO.Response.MunicipalityResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.MunicipalityService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "Api/Admin")
public class AdminController {
    @Autowired
    private MunicipalityService municipalityService;

    @GetMapping("/getAllMunicipalityRequests")
    public ResponseEntity<List<MunicipalityResponse>> getAllMunicipalityRequests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        List<MunicipalityResponse> responses = this.municipalityService.getMunicipalityRequests();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("api/appprove/or/reject/municipalityRequest")
    public ResponseEntity<String> approveOrRejectMunicipalityRequest(
            @RequestParam long idMunicipality,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();
        String id = userDetails.getId();
        long idUser = Long.parseLong(id);
        String role = userDetails.getRole();
        String municipality = userDetails.getMunicipality();
        String visitedMunicipality = userDetails.getVisitedMunicipality();

        this.municipalityService.approveOrRejectMunicipalityRequest(idMunicipality, status);

        return ResponseEntity.ok("Operazione eseguita con successo");
    }
}
