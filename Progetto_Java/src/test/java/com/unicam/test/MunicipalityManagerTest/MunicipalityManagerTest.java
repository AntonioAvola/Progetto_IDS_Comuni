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

import static org.hamcrest.Matchers.hasSize;
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

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testAddMunicipalityUnauthorizedAdmin() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testAddMunicipalityUnauthorizedAnimator() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testAddMunicipalityUnauthorizedCurator() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testAddMunicipalityUnauthorizedContributor() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testAddMunicipalityUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor2", idUser = 10,  municipality = "ROMA", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testAddMunicipalityUnauthorizedTurist() throws Exception {
        mockMvc.perform(post("/api/municipalityManager/add/municipality")
                        .param("description", ""))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testValidationPromotionRequestUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testValidationPromotionRequestUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testValidationPromotionRequestUnauthorizedCurator() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationPromotionRequestUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationPromotionRequestUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor2", idUser = 10,  municipality = "ROMA", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationPromotionRequestUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/role/promotion")
                        .param("idPromotion", "1")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testGetAllActivityPendingSuccessfull() throws Exception {
        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['event']").isArray())
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(3))
                .andExpect(jsonPath("$.contents['event'][1].id").value(4))
                .andExpect(jsonPath("$.contents['contest']", hasSize(2)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(3))
                .andExpect(jsonPath("$.contents['contest'][1].id").value(4));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllActivityPendingUnauthorizedAdmin() throws Exception {
        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testGetAllActivityPendingUnauthorizedAnimator() throws Exception {
        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllActivityPendingUnauthorizedCurator() throws Exception {
        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllActivityPendingUnauthorizedContributor() throws Exception {
        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllActivityPendingUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testGetAllActivityPendingUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(get("/api/municipalityManager/view/all/activity/pending"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testValidationActivityPendingUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "EVENT")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "CONTEST")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testValidationActivityPendingUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "EVENT")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "CONTEST")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testValidationActivityPendingUnauthorizedCurator() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "EVENT")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "CONTEST")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationActivityPendingUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "EVENT")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "CONTEST")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationActivityPendingUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "3")
                        .param("type", "EVENT")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "CONTEST")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }
    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8,  municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testValidationActivityPendingUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "EVENT")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/municipalityManager/approve/or/reject/activity")
                        .param("idActivity", "4")
                        .param("type", "CONTEST")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }
}
