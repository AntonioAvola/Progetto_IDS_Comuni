package com.unicam.test.UserTest;

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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }


    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testGetAllMunicipalityApprovedAdmin() throws Exception {
        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityApprovedAnimator() throws Exception {
        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityApprovedCurator() throws Exception {
        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityApprovedContributor() throws Exception {
        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityApprovedContributorAuthorized() throws Exception {
        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testGetAllMunicipalityApprovedMunicipalityManager() throws Exception {
        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "curator3", idUser = 13, municipality = "NAPOLI", roles = "CURATOR", visitedMunicipality = "NAPOLI")
    public void testGetAllMunicipalityApprovedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(get("/api/user/get/all/municipalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value("MILANO"))
                .andExpect(jsonPath("$[1]").value("ROMA"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testRolePromotionRequestFromContributorSuccessfull() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "ANIMATOR")
                        .param("justification", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("Richiesta di promozione inviata"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testRolePromotionRequestFromAuthorizedContributorSuccessfull() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "CURATOR")
                        .param("justification", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("Richiesta di promozione inviata"));
    }

    @Test
    @WithMockUserDetails(username = "animator2", idUser = 9, municipality = "ROMA", roles = "ANIMATOR", visitedMunicipality = "ROMA")
    public void testRolePromotionRequestFromAnimatorSuccessfull() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "CONTRIBUTOR")
                        .param("justification", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("Richiesta di promozione inviata"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testRolePromotionRequestFromCuratorSuccessfull() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "AUTHORIZED_CONTRIBUTOR")
                        .param("justification", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("Richiesta di promozione inviata"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testRolePromotionRequestFailed() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "ANIMATOR")
                        .param("justification", ""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La tua richiesta di promozione è ancora in attesa di validazione!"));
    }

    @Test
    @WithMockUserDetails(username = "animator2", idUser = 9, municipality = "ROMA", roles = "ANIMATOR", visitedMunicipality = "ROMA")
    public void testDeleteAccountAnimator() throws Exception {
        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account eliminato con successo"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testDeleteAccountCurator() throws Exception {
        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account eliminato con successo"));
    }

    @Test
    @WithMockUserDetails(username = "contributor2", idUser = 10, municipality = "ROMA", roles = "CONTRIBUTOR", visitedMunicipality = "ROMA")
    public void testDeleteAccountContributor() throws Exception {
        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account eliminato con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor2", idUser = 11, municipality = "ROMA", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "ROMA")
    public void testDeleteAccountContributorAuthorized() throws Exception {
        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account eliminato con successo"));
    }

    @Test
    @WithMockUserDetails(username = "animator2", idUser = 9, municipality = "ROMA", roles = "ANIMATOR", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityAnimator() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityCurator() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));
    }

    @Test
    @WithMockUserDetails(username = "contributor2", idUser = 10, municipality = "ROMA", roles = "CONTRIBUTOR", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityContributor() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor2", idUser = 11, municipality = "ROMA", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "ROMA"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testRolePromotionRequestUnauthorizedAdmin() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "ANIMATOR")
                        .param("justification", ""))
                .andExpect(status().isUnauthorized()) // Verifica HTTP 401
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testRolePromotionRequestUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "CURATOR")
                        .param("justification", ""))
                .andExpect(status().isUnauthorized()) // Verifica HTTP 401
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator3", idUser = 14, municipality = "NAPOLI", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testRolePromotionRequestUnauthorizedTurist() throws Exception {
        mockMvc.perform(post("/api/user/role/promotion/request")
                        .contentType(APPLICATION_JSON)
                        .param("newRole", "CURATOR")
                        .param("justification", ""))
                .andExpect(status().isUnauthorized()) // Verifica HTTP 401
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 8, municipality = "ROMA", roles = "ADMIN", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager2", idUser = 7, municipality = "ROMA", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "ROMA")
    public void testVisitMunicipalityUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testDeleteAccountUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 8, municipality = "ROMA", roles = "ADMIN", visitedMunicipality = "ROMA")
    public void testDeleteAccountUnauthorizedAdmin() throws Exception {
        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager2", idUser = 7, municipality = "ROMA", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "ROMA")
    public void testDeleteAccountUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(delete("/api/user/delete/account"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator2", idUser = 9, municipality = "ROMA", roles = "ANIMATOR", visitedMunicipality = "ROMA")
    public void testPartecipateToContestTuristAnimator() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Partecipazione aggiunta con successo"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testPartecipateToContestCurator() throws Exception {
        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Partecipazione aggiunta con successo"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testPartecipateToContestContributor() throws Exception {
        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Partecipazione aggiunta con successo"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testPartecipateToContestContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Partecipazione aggiunta con successo"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testPartecipateToContestTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Partecipazione aggiunta con successo"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testPartecipateToContestUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testPartecipateToContestUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testPartecipateToContestUnauthorizedOWNAnimator() throws Exception {
        mockMvc.perform(put("/api/user/partecipate/contest")
                        .param("idContest", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testContentsActivitiesMunicipalityAnimator() throws Exception {
        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(3)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(1))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(4)))
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(1))
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(2))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point'][3].id").value(4))
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(1))
                .andExpect(jsonPath("$.contents['event'][1].id").value(2))
                .andExpect(jsonPath("$.contents['contest']", hasSize(1)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(2));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testContentsActivitiesMunicipalityCurator() throws Exception {
        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(3)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(1))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(4)))
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(1))
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(2))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point'][3].id").value(4))
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(1))
                .andExpect(jsonPath("$.contents['event'][1].id").value(2))
                .andExpect(jsonPath("$.contents['contest']", hasSize(1)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(2));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testContentsActivitiesMunicipalityContributor() throws Exception {
        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(3)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(1))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(4)))
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(1))
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(2))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point'][3].id").value(4))
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(1))
                .andExpect(jsonPath("$.contents['event'][1].id").value(2))
                .andExpect(jsonPath("$.contents['contest']", hasSize(1)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(2));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testContentsActivitiesMunicipalityContributorAuthorized() throws Exception {
        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(3)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(1))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(4)))
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(1))
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(2))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point'][3].id").value(4))
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(1))
                .andExpect(jsonPath("$.contents['event'][1].id").value(2))
                .andExpect(jsonPath("$.contents['contest']", hasSize(1)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(2));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testContentsActivitiesMunicipalityMunicipalityManager() throws Exception {
        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(3)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(1))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(4)))
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(1))
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(2))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point'][3].id").value(4))
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(1))
                .andExpect(jsonPath("$.contents['event'][1].id").value(2))
                .andExpect(jsonPath("$.contents['contest']", hasSize(1)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(2));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testContentsActivitiesMunicipalityTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(3)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(1))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(4)))
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(1))
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(2))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(3))
                .andExpect(jsonPath("$.contents['interest point'][3].id").value(4))
                .andExpect(jsonPath("$.contents['event']", hasSize(2)))
                .andExpect(jsonPath("$.contents['event'][0].id").value(1))
                .andExpect(jsonPath("$.contents['event'][1].id").value(2))
                .andExpect(jsonPath("$.contents['contest']", hasSize(1)))
                .andExpect(jsonPath("$.contents['contest'][0].id").value(2));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testContentsActivitiesMunicipalityUnauthorizedAdmin() throws Exception {
        mockMvc.perform(get("/api/user/view/all/content/by/municipality"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }
}
