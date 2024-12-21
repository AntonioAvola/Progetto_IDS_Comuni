package com.unicam.test.SecurityContext.Factory;

import com.unicam.test.SecurityContext.WithMockUserDetails;
import com.unicam.test.SecurityContext.WithMockUserDetailsSecurityContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@Configuration
public class TestSecurityConfig {

    @Bean
    public WithSecurityContextFactory<WithMockUserDetails> withMockUserDetailsSecurityContextFactory() {
        // Restituisce una nuova istanza della fabbrica di contesto personalizzata
        return new WithMockUserDetailsSecurityContextFactory();
    }
}

