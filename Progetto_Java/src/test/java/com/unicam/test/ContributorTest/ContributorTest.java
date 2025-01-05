package com.unicam.test.ContributorTest;

import com.unicam.Security.DataInizializer;
import com.unicam.test.SecurityContext.WithMockUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddRequestPOISuccessful() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("type", "SQUARE")
                        .param("openHour", "00")
                        .param("openMinute", "00")
                        .param("closeHour", "00")
                        .param("closeMinute", "00")
                        .param("title", "monumento")
                        .param("description", "monumento storico")
                        .param("reference", "duomo"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse aggiunto con successo"));

        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("type", "SQUARE")
                        .param("openHour", "00")
                        .param("openMinute", "00")
                        .param("closeHour", "00")
                        .param("closeMinute", "00")
                        .param("title", "monumento")
                        .param("description", "monumento storico")
                        .param("reference", "torre velasca"))
                .andExpect(status().isOk()) //risposta HTTP 200 OK
                .andExpect(content().string("Punto di interesse aggiunto con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddPOISuccessful() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("type", "SQUARE")
                        .param("openHour", "00")
                        .param("openMinute", "00")
                        .param("closeHour", "00")
                        .param("closeMinute", "00")
                        .param("title", "monumento")
                        .param("description", "monumento storico")
                        .param("reference", "torre velasca"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse aggiunto con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddPOIFailed() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("type", "SQUARE")
                        .param("openHour", "00")
                        .param("openMinute", "00")
                        .param("closeHour", "00")
                        .param("closeMinute", "00")
                        .param("title", "monumento")
                        .param("description", "monumento storico")
                        .param("reference", "teatro alla scala"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Esiste già un punto di interesse per questo determinato punto geolocalizzato"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testGetAllPOIContributorSuccessful() throws Exception {
        mockMvc.perform(get("/api/contributor/get/all/POI/of/own/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[3].id").value(4));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 4,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testGetAllPOIContributorAuthorizedSuccessful() throws Exception {
        mockMvc.perform(get("/api/contributor/get/all/POI/of/own/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[3].id").value(4));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddItinerarySuccessful() throws Exception {
        mockMvc.perform(post("/api/contributor/add/itinerary")
                        .contentType(APPLICATION_JSON)
                        .content("""
				{
					"title": "corsetta",
					"description": "simpatico giro panoramico del comune",
					"path": [1, 3]
				}
				""")) // Corpo della richiesta
                .andExpect(status().isOk()) //risposta HTTP 200 OK
                .andExpect(content().string("Itinerario aggiunto con successo"));

        mockMvc.perform(post("/api/contributor/add/itinerary")
                        .contentType(APPLICATION_JSON)
                        .content("""
				{
					"title": "corsetta",
					"description": "simpatico giro panoramico del comune",
					"path": [1, 2]
				}
				""")) // Corpo della richiesta
                .andExpect(status().isOk()) //risposta HTTP 200 OK
                .andExpect(content().string("Itinerario aggiunto con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddItineraryFailed() throws Exception {
        mockMvc.perform(post("/api/contributor/add/itinerary")
                        .contentType(APPLICATION_JSON)
                        .content("""
				{
					"title": "corsetta",
					"description": "simpatico giro panoramico del comune",
					"path": [1, 1]
				}
				""")) // Corpo della richiesta
                .andExpect(status().isBadRequest()) //risposta HTTP 200 OK
                .andExpect(jsonPath("$.message").value("Nella lista di punti di interesse sono presenti meno di due punti di interesse"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddRequestItinerarySuccessful() throws Exception {
        mockMvc.perform(post("/api/contributor/add/itinerary")
                        .contentType(APPLICATION_JSON)
                        .content("""
				{
					"title": "corsetta",
					"description": "simpatico giro panoramico del comune",
					"path": [1, 3]
				}
				""")) // Corpo della richiesta
                .andExpect(status().isOk()) //risposta HTTP 200 OK
                .andExpect(content().string("Itinerario aggiunto con successo"));

        mockMvc.perform(post("/api/contributor/add/itinerary")
                        .contentType(APPLICATION_JSON)
                        .content("""
				{
					"title": "corsetta",
					"description": "simpatico giro panoramico del comune",
					"path": [1, 2]
				}
				""")) // Corpo della richiesta
                .andExpect(status().isOk()) //risposta HTTP 200 OK
                .andExpect(content().string("Itinerario aggiunto con successo"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddRequestItineraryFailed() throws Exception {
        mockMvc.perform(post("/api/contributor/add/itinerary")
                        .contentType(APPLICATION_JSON)
                        .content("""
				{
					"title": "corsetta",
					"description": "simpatico giro panoramico del comune",
					"path": [1, 1]
				}
				""")) // Corpo della richiesta
                .andExpect(status().isBadRequest()) //risposta HTTP 200 OK
                .andExpect(jsonPath("$.message").value("Nella lista di punti di interesse sono presenti meno di due punti di interesse"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    void testAddRequestPOIFailed() throws Exception {
        mockMvc.perform(post("/api/contributor/add/interestPoint")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("type", "SQUARE")
                        .param("openHour", "00")
                        .param("openMinute", "00")
                        .param("closeHour", "00")
                        .param("closeMinute", "00")
                        .param("title", "monumento")
                        .param("description", "monumento storico")
                        .param("reference", "teatro alla scala"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Esiste già un punto di interesse per questo determinato punto geolocalizzato"));
    }
}
