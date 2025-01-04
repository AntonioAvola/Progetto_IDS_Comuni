package com.unicam.test.AdminTest;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AdminTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void validationMunicipalityRequestPending() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "4")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk());
    }
}
