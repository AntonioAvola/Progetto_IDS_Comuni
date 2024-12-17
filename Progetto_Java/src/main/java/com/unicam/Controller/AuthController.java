package com.unicam.Controller;


import com.unicam.DTO.Request.SingInDTO;
import com.unicam.Entity.Role;
import com.unicam.Service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password){
        String token = userService.login(username, password);
        //viene restituito il token dell'account
        return ResponseEntity.ok(token);
    }

    @PostMapping("/singIn")
    public ResponseEntity<?> singIn(
            @RequestBody SingInDTO request,
            @Parameter(description = "Scelta del Ruolo",
                    schema = @Schema(type = "Role", allowableValues = {"CURATOR", "ANIMATOR", "MUNICIPALITY_MANAGER",
                            "AUTHORIZED_CONTRIBUTOR", "CONTRIBUTOR"}))
            @RequestParam(defaultValue = "CONTRIBUTOR") Role role){
        String token = userService.singIn(request, role);
        //viene restituito il token dell'account
        return ResponseEntity.ok(token);
    }
}
