package com.unicam.test.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})  // Può essere applicata ai metodi e alle classi
@Retention(RetentionPolicy.RUNTIME)  // L'annotazione sarà disponibile a runtime
@WithMockUser  // Estende l'annotazione esistente @WithMockUser
@WithSecurityContext(factory = WithMockUserDetailsSecurityContextFactory.class)
public @interface WithMockUserDetails {

    String username() default "user";  // Nome utente di default
    long idUser() default 0;  // ID personalizzato dell'utente
    String roles() default "USER";  // Ruolo predefinito dell'utente
    String municipality() default "UNKNOWN";  // Comune personalizzato
    String visitedMunicipality() default  "UNKNOWN"; //Comune visitato personalizzato dell'utente
}

