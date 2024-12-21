package com.unicam.test.SecurityContext;
import com.unicam.Security.UserCustomDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class WithMockUserDetailsSecurityContextFactory
        implements WithSecurityContextFactory<WithMockUserDetails> {

    @Override
    public SecurityContext createSecurityContext(WithMockUserDetails annotation) {
        // Crea un'istanza dei dettagli dell'utente personalizzati
        UserCustomDetails userDetails = new UserCustomDetails(
                annotation.username(),
                String.valueOf(annotation.idUser()),
                annotation.municipality(),
                annotation.roles(),
                annotation.visitedMunicipality()
        );

        // Crea l'oggetto Authentication usando i dettagli personalizzati
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Crea e imposta il contesto di sicurezza
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
