package com.unicam.Controller;


import com.unicam.DTO.Request.SingInDTO;
import com.unicam.Service.UserService;
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
        //TODO implementare la visualizzazione di tutti i contenuti del proprio comune, quindi approvati
    }

    @PostMapping("/singIn")
    public ResponseEntity<?> singIn(@RequestBody SingInDTO request){
        String token = userService.singIn(request);
        //viene restituito il token dell'account
        return ResponseEntity.ok(token);
        //TODO implementare la visualizzazione di tutti i contenuti del proprio comune, quindi approvati
    }
}
