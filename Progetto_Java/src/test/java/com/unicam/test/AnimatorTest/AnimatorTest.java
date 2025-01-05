package com.unicam.test.AnimatorTest;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AnimatorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testAddEventSuccessful() throws Exception {
        mockMvc.perform(post("/api/animator/add/event")
                        .contentType(APPLICATION_JSON)
                        .content("""
				 {
					"title": "stelle cadenti",
					"description": "osservazione stelle cadenti",
					"idReference": 4,
					"start": "2025-02-20T20:00:00",
					"end": "2025-02-21T03:00:00"
				}
				"""))
                .andExpect(status().isOk())
                .andExpect(content().string("Proposta di evento inviata con successo"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testAddContestSuccessful() throws Exception {
        mockMvc.perform(post("/api/animator/add/contest")
                        .contentType(APPLICATION_JSON)
                        .content("""
				 {
					"title": "fotografia",
					"description": "fotografia più bella",
					"reward": "€150",
					"start": "2025-02-25T16:00:00",
					"end": "2025-02-28T18:00:00"
				}
				"""))
                .andExpect(status().isOk())
                .andExpect(content().string("Proposta di contest inviata con successo"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testAddEventFailed() throws Exception {
        mockMvc.perform(post("/api/animator/add/event")
                        .contentType(APPLICATION_JSON)
                        .content("""
			 {
				"title": "stelle cadenti",
				"description": "osservazione stelle cadenti",
				"idReference": 6,
				"start": "2025-02-24T20:00:00",
				"end": "2025-02-21T03:00:00"
			}
			"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Inizio e Fine non conformi"));

        mockMvc.perform(post("/api/animator/add/event")
                        .contentType(APPLICATION_JSON)
                        .content("""
			 {
				"title": "stelle cadenti",
				"description": "osservazione stelle cadenti",
				"idReference": 3,
				"start": "2025-01-08T20:00:00",
				"end": "2025-01-15T03:00:00"
			}
			"""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Sovrapposizione durata con un evento già approvato per questo riferimento"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testAddContestFailed() throws Exception {
        mockMvc.perform(post("/api/animator/add/contest")
                        .contentType(APPLICATION_JSON)
                        .content("""
			 {
				"title": "fotografia",
				"description": "fotografia più bella",
				"reward": "€150",
				"start": "2025-02-27T16:00:00",
				"end": "2025-02-25T18:00:00"
			}
			"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Inizio e Fine non conformi"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testGetAllGeoPointSuccessful() throws Exception {
        mockMvc.perform(get("/api/animator/get/all/geopoint"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[3].id").value(4));
    }

}
