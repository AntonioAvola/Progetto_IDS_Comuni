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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    public void testValidationMunicipalityRequestApproved() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testValidationMunicipalityRequestRejected() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testGetAllMunicipalityRequestSuccessfull() throws Exception {
        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(3));
    }
    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testValidationMunicipalityRequestUnauthorizedAnimator() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testValidationMunicipalityRequestUnauthorizedCurator() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationMunicipalityRequestUnauthorizedContributor() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationMunicipalityRequestUnauthorizedContributoAuthorized() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testValidationMunicipalityRequestUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8,  municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testValidationMunicipalityRequestUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(post("/api/admin/appprove/or/reject/municipality/request")
                        .param("idMunicipality", "3")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityRequestUnauthorizedAnimator() throws Exception {
        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityRequestUnauthorizedCurator() throws Exception {
        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityRequestUnauthorizedContributor() throws Exception {
        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityRequestUnauthorizedContributorAuthorizedr() throws Exception {
        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityRequestUnauthorizedMunicipalityMAnager() throws Exception {
        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testGetAllMunicipalityRequestUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(get("/api/admin/get/all/municipality/requests"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }
}
