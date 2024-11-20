package com.unicam.Security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Qui puoi recuperare l'utente dal database, ad esempio:
        if ("user".equals(username)) {
            return User.withUsername(username)
                    .password("{noop}password") // usa un password encoder appropriato
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
