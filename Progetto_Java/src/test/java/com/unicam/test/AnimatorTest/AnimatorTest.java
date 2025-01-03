package com.unicam.test.AnimatorTest;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
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
					"reference": 6,
					"start": "2025-02-20T20:00:00",
					"end": "2025-02-21T03:00:00"
				}
				""")) // Corpo della richiesta
                .andExpect(status().isOk()) //risposta HTTP 200 OK
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
				""")) // Corpo della richiesta
                .andExpect(status().isOk()) //risposta HTTP 200 OK
                .andExpect(content().string("Proposta di contest inviata con successo"));
    }


}
