package com.unicam.test.MunicipalityManagerTest;

import com.unicam.Security.DataInizializer;
import com.unicam.test.SecurityContext.WithMockUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
    public void validationActivityPending() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "EVENT")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "EVENT")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "CONTEST")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "CONTEST")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void PromotionRequest() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());
    }
}
