package com.unicam.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
            "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
            "/configuration/ui", "/configuration/security", "/swagger-ui/**",
            "/webjars/**", "/swagger-ui.html", "api/auth/**", "/api/test/**",
            "/authenticate", "/api/utente/Api/RegistrazioneUtente",
            "/Api/GestorePiattaforma/Registrazione"
    };

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF per le API stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura sessioni stateless
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST_URL).permitAll()  // Percorsi consentiti senza autenticazione
                        .anyRequest().authenticated()                 // Richiedi autenticazione per tutte le altre richieste
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Aggiungi il filtro JWT
                .build(); // Costruisce la configurazione
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
