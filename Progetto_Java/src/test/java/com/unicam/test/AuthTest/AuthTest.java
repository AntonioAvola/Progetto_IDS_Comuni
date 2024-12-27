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

    @Test
    void testLoginFailed() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .param("username", "adin")
                        .param("password", "Admin1!"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Non esiste alcun utente con l'username passato"));

        mockMvc.perform(post("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "Ad1!"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Password errata"));

        mockMvc.perform(post("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "Admin!"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Password errata"));
    }

    @Test
    void testSingInFailed() throws Exception {
        //username già in uso
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "admin",
						"name": "elisa",
						"email": "eli@gmail.com",
						"password": "Au12!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'username è già in uso"));

        //email già in uso
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "elisa",
						"email": "admin@admin.com",
						"password": "Au12!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'email è già in uso"));

        //campo username vuoto o solamente spazi
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "",
						"name": "elisa",
						"email": "eli@gmail.com",
						"password": "Au12!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'username non è stato inserito"));

        //campo name vuoto o solamente spazi
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "",
						"email": "eli@gmail.com",
						"password": "Au12!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Il nome non è stato inserito"));

        //campo email vuoto o solamente spazi
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "elisa",
						"email": "",
						"password": "Au12!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'email non è stata inserita"));

        //email formato non corretto
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "elisa",
						"email": "eligmail.com",
						"password": "Au12!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("L'email non è stata inserita correttamente. " +
                        "Si prega di inserire una email valida"));

        //campo password vuoto o solamente spazi
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "elisa",
						"email": "eli@gmail.com",
						"password": "     ",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La password non è stata inserita"));

        //password che non rispetta tutti i requisiti
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "elisa",
						"email": "eli@gmail.com",
						"password": "Au1!",
						"municipality": "Roma"
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La password non rispetta i requisiti richiesti: lunghezza 5, almeno una maiuscola, " +
                        "almeno una minuscola, almeno un numero, almeno un simbolo speciale, niente spazi vuoti"));

        //campo municipality vuoto o solamente spazi
        mockMvc.perform(post("/api/auth/singIn")
                        .contentType(APPLICATION_JSON)
                        .content("""
					{
						"username": "eli",
						"name": "elisa",
						"email": "eli@gmail.com",
						"password": "Au12!",
						"municipality": ""
					}
					""")
                        .param("role", "CONTRIBUTOR"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Il Comune di residenza non è stato inserito"));
    }
}
