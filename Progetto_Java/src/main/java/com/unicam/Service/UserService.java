package com.unicam.Service;

import com.unicam.DTO.Request.SingInDTO;
import com.unicam.Entity.Role;
import com.unicam.Entity.RolePromotion;
import com.unicam.Entity.User;
import com.unicam.Repository.UserRepository;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Service.Content.*;
import com.unicam.Validators.EmailValidator;
import com.unicam.Validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

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

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PromotionService promotionService;

    private JwtTokenProvider tokenProvider = new JwtTokenProvider();


    public String login(String username, String password){
        checkCredentialsDB(username, password);

        User user = repoUser.findByUsername(username);
        user.setVisitedMunicipality(user.getMunicipality());
        this.repoUser.save(user);
        return tokenProvider.createToken(user);
    }

    public String singIn(SingInDTO singInDTO, Role role){
        User user = singInDTO.toEntity(role);
        checkUserFields(user);
        nameEmailAlreadyExists(user);
        user.HashPassword();
        this.repoUser.save(user);
        return tokenProvider.createToken(user);
    }

    public void deleteAccount(long id){
        User user = this.repoUser.findUserById(id);
        this.contestService.removeContestUser(user);
        this.eventService.removeEventUser(user);
        this.itineraryService.removeItineraryUser(user);
        this.reviewService.removeReviewUser(user);
        this.interestPointService.removeInterestPointUser(user);
        this.promotionService.removePromotionUser(user);
        this.repoUser.delete(user);
    }

    public void nameEmailAlreadyExists(User user){
        User userFound = repoUser.findByUsername(user.getUsername());
        if (userFound != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'username è già in uso");
        userFound = repoUser.findByEmail(user.getEmail());
        if(userFound != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email è già in uso");
    }

    private void checkCredentialsDB(String username, String password){
        User utenteLogin = repoUser.findByUsername(username);
        if(utenteLogin == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Non esiste alcun utente con l'username passato");
        if(!checkPassword(password, utenteLogin.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password errata");
    }

    private boolean checkPassword(String password, String checkPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, checkPassword);
    }

    private void checkUserFields(User user){
        if(user.getName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il nome non è stato inserito");
        if(user.getUsername().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'username non è stato inserito");
        if(user.getMunicipality().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Il Comune di residenza non è stato inserito");
        if(user.getEmail().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email non è stata inserita");
        if(!EmailValidator.isValidEmail(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email non è stata inserita correttamente. Si prega di inserire una email valida");
        if(user.getPassword().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La password non è stata inserita");
        if(!PasswordValidator.isValidPassword(user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La password non rispetta i requisiti richiesti: lunghezza 5, almeno una maiuscola, " +
                    "almeno una minuscola, almeno un numero, almeno un simbolo speciale, niente spazi vuoti");
    }

    public User getUser(long id) {
        return this.repoUser.findUserById(id);
    }

    public void updateRole(long idPromotion) {
        RolePromotion promotion = this.promotionService.getPromotion(idPromotion);
        User user = this.repoUser.findUserById(promotion.getUser().getId());
        user.setRole(promotion.getPromotion());
        this.repoUser.save(user);
    }

    public void visitMunicipality(String newMunicipality, long idUser) {
        User user = this.repoUser.findUserById(idUser);
        user.setVisitedMunicipality(newMunicipality);
        this.repoUser.save(user);
    }
}
