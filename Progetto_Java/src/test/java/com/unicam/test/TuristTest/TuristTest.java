package com.unicam.test.TuristTest;

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

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TuristTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataInizializer dataInitializer;

    @BeforeEach
    public void setUp() throws Exception{
        dataInitializer.run();
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testAddToFavoritesSuccessful() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenuto aggiunto con successo ai preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenuto aggiunto con successo ai preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenuto aggiunto con successo ai preferiti"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testAddToFavoritesFailed() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenuto aggiunto con successo ai preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Il punto di interesse è già tra i preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenuto aggiunto con successo ai preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("L'itinerario è già presente tra i preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenuto aggiunto con successo ai preferiti"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Evento già presente tra i preferiti"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testtAddToFavoritesUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testtAddToFavoritesUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testtAddToFavoritesUnauthorizedACurator() throws Exception {
        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testtAddToFavoritesUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testtAddToFavoritesUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testtAddToFavoritesUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "ITINERARY")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/add/to/favorite")
                        .param("type", "EVENT")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testReportContentSuccessful() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Punto di interesse segnalato con successo"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Itinerario segnalato con successo"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testReportContentUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testReportContentUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testReportContentUnauthorizedACurator() throws Exception {
        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testReportContentUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testReportContentUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testReportContentUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "INTEREST POINT")
                        .param("idContent", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));

        mockMvc.perform(put("/api/turist/reported")
                        .param("type", "ITINERARY")
                        .param("idContent", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
    public void testAddReviewSuccessful() throws Exception {
        mockMvc.perform(put("/api/user/visited/municipality")
                        .param("newMunicipality", "MILANO"))
                .andExpect(status().isOk())
                .andExpect(content().string("Visita il comune eseguita con successo"));

        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Recensione aggiunta con successo"));
    }

    @Test
    @WithMockUserDetails(username = "admin", idUser = 1, municipality = "", roles = "ADMIN", visitedMunicipality = "")
    public void testAddReviewUnauthorizedAdmin() throws Exception {
        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4, municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    public void testAddReviewUnauthorizedAnimator() throws Exception {
        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "curator1", idUser = 3, municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
    public void testAddReviewUnauthorizedACurator() throws Exception {
        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "contributor1", idUser = 5, municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testAddReviewUnauthorizedContributor() throws Exception {
        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "authorizedContributor1", idUser = 6, municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
    public void testAddReviewUnauthorizedContributorAuthorized() throws Exception {
        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }

    @Test
    @WithMockUserDetails(username = "manager1", idUser = 2, municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
    public void testAddReviewUnauthorizedMunicipalityManager() throws Exception {
        mockMvc.perform(put("/api/turist/add/review")
                        .contentType(MULTIPART_FORM_DATA_VALUE)
                        .param("title", "esperienza fantastica")
                        .param("description", "esperienza personale durante la mia permanenza nel comune di Milano")
                        .param("referencePOI", "2"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
    }
}
