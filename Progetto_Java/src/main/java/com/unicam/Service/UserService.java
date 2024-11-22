package com.unicam.Service;

import com.unicam.DTO.Request.SingInDTO;
import com.unicam.Entity.User;
import com.unicam.Repository.UserRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Service.Content.*;
import com.unicam.Validators.EmailValidator;
import com.unicam.Validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repoUser;

    @Autowired
    private ContestService contestService;

    @Autowired
    private EventService eventService;

    @Autowired
    private InterestPointService interestPointService;

    @Autowired
    private ItineraryService itineraryService;

    private JwtTokenProvider tokenProvider = new JwtTokenProvider();

    //TODO inserire reviews

    public String login(String username, String password){
        try {
            checkCredentialsDB(username, password);

            User user = repoUser.findByUsername(username);
            user.setVisitedMunicipality(user.getMunicipality());
            this.repoUser.save(user);
            return tokenProvider.createToken(user);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid credentials", e);
        }
    }

    public String singIn(SingInDTO singInDTO){
        User user = singInDTO.toEntity();
        checkUserFields(user);
        nameEmailAlreadyExists(user);
        user.HashPassword();
        this.repoUser.save(user);
        return tokenProvider.createToken(user);
    }

    public void logout(){}

    public void deleteAccount(long id){
        User user = this.repoUser.findUserById(id);
        contestService.removeContestUser(user);
        eventService.removeEventUser(user);
        itineraryService.removeItineraryUser(user);
        interestPointService.removeInterestPointUser(user);
        this.repoUser.delete(user);
    }

    public void addAccount(User user) {
        this.repoUser.save(user);
    }

    public void nameEmailAlreadyExists(User user){
        User userFound = repoUser.findByUsername(user.getUsername());
        if (userFound != null)
            throw new IllegalArgumentException("L'username è già in uso");
        userFound = repoUser.findByEmail(user.getEmail());
        if(userFound != null)
            throw new IllegalArgumentException("L'email è già in uso");
    }

    private void checkCredentialsDB(String username, String password){
        User utenteLogin = repoUser.findByUsername(username);
        if(utenteLogin == null)
            throw new NullPointerException("Non esiste alcun utente con l'username passato");
        if(!checkPassword(password, utenteLogin.getUsername()))
            throw new IllegalArgumentException("Password errata");
    }

    private boolean checkPassword(String password, String username){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, this.repoUser.findByUsername(username).getPassword());
    }

    private void checkUserFields(User user){
        if(user == null)
            throw new NullPointerException("L'utente passato è nullo");
        if(user.getName().isBlank())
            throw new IllegalArgumentException("Il nome non è stato inserito");
        if(user.getUsername().isBlank())
            throw new IllegalArgumentException("L'username non è stato inserito");
        if(user.getMunicipality().isBlank())
            throw new IllegalArgumentException("Il Comune di residenza non è stato inserito");
        if(user.getEmail().isBlank())
            throw new IllegalArgumentException("L'email non è stata inserita");
        if(!EmailValidator.isValidEmail(user.getEmail()))
            throw new IllegalArgumentException("L'email non è stata inserita correttamente. Si prega di inserire una email valida");
        if(user.getPassword().isBlank())
            throw new IllegalArgumentException("La password non è stata inserita");
        if(!PasswordValidator.isValidPassword(user.getPassword()))
            throw new IllegalArgumentException("La password non rispetta i requisiti richiesti: lunghezza 5, almeno una maiuscola, " +
                    "almeno una minuscola, almeno un numero, almeno un simbolo speciale, niente spazi vuoti");
    }

    public User getUser(long id) {
        return this.repoUser.findUserById(id);
    }
}
