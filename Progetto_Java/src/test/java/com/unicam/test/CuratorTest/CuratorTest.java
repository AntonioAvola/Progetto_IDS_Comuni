package com.unicam.test.CuratorTest;

import com.unicam.Security.DataInizializer;
import com.unicam.test.SecurityContext.WithMockUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllPendingContentsSuccessfull() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(2)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(4))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(5))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(3))) // Esattamente 2 elementi
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(5)) // Primo elemento ha id 1
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(6))
                .andExpect(jsonPath("$.contents['interest point'][2].id").value(7));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testGetAllPendingContentsUnauthorizedAdmin() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testGetAllPendingContentsUnauthorizedAnimator() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllPendingContentsUnauthorizedContributor() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllPendingContentsUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testGetAllPendingContentsUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllPendingContentsUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(get("/api/curator/view/all/content/pending")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllReportedContentsSuccessfull() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents['interest point']").isArray())
                .andExpect(jsonPath("$.contents['itinerary']", hasSize(2)))
                .andExpect(jsonPath("$.contents['itinerary'][0].id").value(2))
                .andExpect(jsonPath("$.contents['itinerary'][1].id").value(3))
                .andExpect(jsonPath("$.contents['interest point']", hasSize(2))) // Esattamente 2 elementi
                .andExpect(jsonPath("$.contents['interest point'][0].id").value(3)) // Primo elemento ha id 1
                .andExpect(jsonPath("$.contents['interest point'][1].id").value(4));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testGetAllReportedContentsUnauthorizedAdmin() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testGetAllReportedContentsUnauthorizedAnimator() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllReportedContentsUnauthorizedContributor() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testGetAllReportedContentsUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testGetAllReportedContentsUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testGetAllReportedContentsUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(get("/api/curator/view/all/reported/contents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testValidationContentReported() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario approvato con successo"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "2")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario rifiutato"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse approvato con successo"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "4")
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse rifiutato"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testValidationContentPendingUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "6")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "4")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testValidationContentPendingUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "6")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "5")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationContentPendingUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "7")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "4")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationContentPendingUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "7")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "5")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testValidationContentPendingUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "6")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "5")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8,  municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testValidationContentPendingUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "7")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/pending/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "4")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testValidationContentReportedUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testValidationContentReportedUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "2")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationContentReportedUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "4")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testValidationContentReportedUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "4")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "2")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testValidationContentReportedUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "4")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "2")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8,  municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testValidationContentReportedUnauthorizedTurist() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "3")
                        .param("status", "REJECTED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/curator/approve/or/reject/reported/content")
                        .param("type", "ITINERARY")
                        .param("idContent", "3")
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }
}
