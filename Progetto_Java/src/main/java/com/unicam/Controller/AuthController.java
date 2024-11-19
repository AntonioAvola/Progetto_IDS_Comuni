package com.unicam.Controller;


import com.unicam.DTO.Request.SingInDTO;
import com.unicam.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(name = "Api/Auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("api/login")
    public void login(@RequestParam String username, @RequestParam String password){
        userService.login(username, password);
        //viene restituito il token dell'account

        //TODO implementare la visualizzazione di tutti i contenuti del proprio comune, quindi approvati
    }

    @PostMapping("api/singIn")
    public void singIn(SingInDTO request){
        userService.singIn(request);
        //viene restituito il token dell'account

        //TODO implementare la visualizzazione di tutti i contenuti del proprio comune, quindi approvati
    }

    @DeleteMapping("api/deleteAccount")
    public void deleteAccount(){
        //TODO chiamare il metodo del servizio dell'utente e passare l'id dell'utente
    }
}
