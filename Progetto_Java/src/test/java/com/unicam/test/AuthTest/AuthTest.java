package com.unicam.test.AuthTest;

import com.unicam.Security.DataInizializer;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //garantisce il rollback delle transazioni dopo ogni test
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    void testLoginSuccessful() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "Admin1!"))
                .andExpect(status().isOk());
    }

    @Test
    void testSingInSuccessful() throws Exception {
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON) // Specifica il tipo di contenuto
                        .content("""
                        {
                            "username": "eli",
                            "name": "elisa",
                            "email": "eli@gmail.com",
                            "password": "Au12!",
                            "municipality": "Roma"
                        }
                        """)
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isOk());
    }
}
