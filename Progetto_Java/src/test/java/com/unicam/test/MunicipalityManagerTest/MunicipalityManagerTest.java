package com.unicam.test.MunicipalityManagerTest;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MunicipalityManagerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testValidationActivityPending() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "EVENT")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Evento approvato con successo"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "EVENT")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Evento rifiutato"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "CONTEST")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contest approvato con successo"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "CONTEST")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contest rifiutato"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testValidationPromotionRequest() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Promozione accettata"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "2")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Promozione rifiutata"));
    }

    @Test
    @WithMockUserDetails(username = "manager4", idUser = 17,  municipality = "FIRENZE", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "FIRENZE")
    public void testAddMunicipalitySuccessfull() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("Richiesta di inserimento del comune di FIRENZE inviata con successo"));
    }

    @Test
    @WithMockUserDetails(username = "manager3", idUser = 12,  municipality = "NAPOLI", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "NAPOLI")
    public void testAddMunicipalityFailed() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Il comune è già presente o è già stata inviata una richiesta"));
    }
}
