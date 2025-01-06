package com.unicam.Controller;

import com.unicam.DTO.Response.MunicipalityResponse;
import com.unicam.Entity.Content.ContentStatus;
import com.unicam.Entity.Role;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.MunicipalityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    private MunicipalityService municipalityService;

    @GetMapping("/get/all/municipality/requests")
    @Operation(summary = "Visualizza tutte le richieste di inserimenti dei comuni nella piattaforma")
    public ResponseEntity<List<MunicipalityResponse>> getAllMunicipalityRequests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        if(!role.equals(Role.ADMIN.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        List<MunicipalityResponse> responses = this.municipalityService.getMunicipalityRequests();
        if(responses.isEmpty()){
            throw new ResponseStatusException(HttpStatus.OK, "Al momento non sono presenti richieste di inserimento di nuovi comuni nella piattaforma");
        }

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/appprove/or/reject/municipality/request")
    @Operation(summary = "Validazione richieste inserimento comune",
            description = "Approva o rifiuta una richiesta di inserimento di un comune nella piattaforma. " +
                    "Usa uno degli ID disponibili da /get/all/municipality/requests.")
    public ResponseEntity<String> validateMunicipalityRequest(
            @RequestParam long idMunicipality,
            @Parameter(description = "Operazione da eseguire",
                    schema = @Schema(type = "ContentStatus", allowableValues = {"APPROVED", "REJECTED"}))
            @RequestParam(defaultValue = "APPROVED") ContentStatus status){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String role = userDetails.getRole();
        if(!role.equals(Role.ADMIN.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Non hai i permessi per eseguire l'operazione");

        this.municipalityService.validateMunicipality(idMunicipality, status);
        if(status.equals(ContentStatus.APPROVED)){
            return ResponseEntity.ok("Comune approvato con successo");
        }
        else{
            return ResponseEntity.ok("Comune rifiutato con successo");
        }
    }
}
