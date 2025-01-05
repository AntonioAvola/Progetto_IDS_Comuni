package com.unicam.test.CuratorTest;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CuratorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testValidationContentPending() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "6")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse approvato con successo"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "7")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse rifiutato"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "4")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario approvato con successo"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "5")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario rifiutato"));
    }
}
