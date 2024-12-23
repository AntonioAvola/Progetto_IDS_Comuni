package com.unicam.test.ContributorTest;

import com.unicam.Security.DataInizializer;
import com.unicam.test.SecurityContext.WithMockUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ContributorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR")
    void testAddRequestPOISuccessful() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(APPLICATION_JSON) // Imposta il tipo di contenuto come JSON
                        .param("type", "SQUARE")
                        .param("openHour", "00") // Ora di apertura
                        .param("openMinute", "00") // Minuti di apertura
                        .param("closeHour", "00") // Ora di chiusura
                        .param("closeMinute", "00") // Minuti di chiusura
                        .content("""
					{
						"title": "monumento",
						"description": "monumento storico",
						"reference": "duomo"
					}
					""")) // Corpo della richiesta
                .andExpect(status().isOk()) // Verifica che la risposta HTTP sia 200 OK
                .andExpect(content().string("Punto di interesse aggiunto con successo"));

        mockMvc.perform(post("/api/contributor/add/interestPoint")
                .contentType(APPLICATION_JSON)
                        .param("type", "SQUARE")
                        .param("openHour", "00") // Ora di apertura
                        .param("openMinute", "00") // Minuti di apertura
                        .param("closeHour", "00") // Ora di chiusura
                        .param("closeMinute", "00") // Minuti di chiusura
                        .content("""
					{
						"title": "monumento",
						"description": "monumento storico",
						"reference": "torre velasca"
					}
					""")) // Corpo della richiesta
                .andExpect(status().isOk()) // Verifica che la risposta HTTP sia 200 OK
                .andExpect(content().string("Punto di interesse aggiunto con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR")
    void testAddPOISuccessful() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(APPLICATION_JSON)
                        .param("type", "SQUARE")
                        .param("openHour", "00") // Ora di apertura
                        .param("openMinute", "00") // Minuti di apertura
                        .param("closeHour", "00") // Ora di chiusura
                        .param("closeMinute", "00") // Minuti di chiusura
                        .content("""
					{
						"title": "monumento",
						"description": "monumento storico",
						"reference": "torre velasca"
					}
					""")) // Corpo della richiesta
                .andExpect(status().isOk()) // Verifica che la risposta HTTP sia 200 OK
                .andExpect(content().string("Punto di interesse aggiunto con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR")
    void testAddPOIFailed() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(APPLICATION_JSON)
                        .param("type", "SQUARE")
                        .param("openHour", "00") // Ora di apertura
                        .param("openMinute", "00") // Minuti di apertura
                        .param("closeHour", "00") // Ora di chiusura
                        .param("closeMinute", "00") // Minuti di chiusura
                        .content("""
					{
						"title": "monumento",
						"description": "monumento storico",
						"reference": "teatro alla scala"
					}
					""")) // Corpo della richiesta
                .andExpect(status().isConflict()) // Verifica che la risposta HTTP sia 200 OK
                .andExpect(jsonPath("$.message").value("Esiste già un punto di interesse per questo determinato punto geolocalizzato"));
    }
}
