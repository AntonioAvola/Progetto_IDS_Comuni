package com.unicam.test.AnimatorTest;

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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
						"idReference": 4,
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
                .andExpect(status().isOk())
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
					"""))
                .andExpect(status().isOk())
                .andExpect(content().string("Proposta di contest inviata con successo"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testAddEventFailed() throws Exception {
        mockMvc.perform(post("/api/animator/add/event")
                        .contentType(APPLICATION_JSON)
                        .content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"idReference": 6,
						"start": "2025-02-24T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Inizio e Fine non conformi"));

        mockMvc.perform(post("/api/animator/add/event")
                        .contentType(APPLICATION_JSON)
                        .content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"idReference": 3,
						"start": "2025-02-24T20:00:00",
						"end": "2025-02-27T03:00:00"
					}
					"""))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Sovrapposizione durata con un evento già approvato per questo riferimento"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testAddContestFailed() throws Exception {
        mockMvc.perform(post("/api/animator/add/contest")
                        .contentType(APPLICATION_JSON)
                        .content("""
					 {
						"title": "fotografia",
						"description": "fotografia più bella",
						"reward": "€150",
						"start": "2025-02-27T16:00:00",
						"end": "2025-02-25T18:00:00"
					}
					"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Inizio e Fine non conformi"));
    }

    @Test
    @WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
    void testGetAllGeoPointSuccessful() throws Exception {
        mockMvc.perform(get("/api/animator/get/all/geopoint"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[3].id").value(4));
    }

	@Test
	@WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testAddActivityUnauthorizedContributor() throws Exception {
		mockMvc.perform(post("/api/animator/add/event")
						.contentType(APPLICATION_JSON)
						.content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"reference": "torre velasca",
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testAddActivityUnauthorizedContributorAuthorized() throws Exception {
		mockMvc.perform(post("/api/animator/add/event")
						.contentType(APPLICATION_JSON)
						.content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"reference": "torre velasca",
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
	void testAddActivityUnauthorizedCurator() throws Exception {
		mockMvc.perform(post("/api/animator/add/event")
						.contentType(APPLICATION_JSON)
						.content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"reference": "torre velasca",
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
	void testAddActivityUnauthorizedMunicipalityManager() throws Exception {
		mockMvc.perform(post("/api/animator/add/event")
						.contentType(APPLICATION_JSON)
						.content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"reference": "torre velasca",
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "MILANO")
	void testAddActivityUnauthorizedAdmin() throws Exception {
		mockMvc.perform(post("/api/animator/add/event")
						.contentType(APPLICATION_JSON)
						.content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"reference": "torre velasca",
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "contributor2", idUser = 10,  municipality = "ROMA", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testAddActivityUnauthorizedTurist() throws Exception {
		mockMvc.perform(put("/api/user/visited/municipality")
						.param("newMunicipality", "MILANO"))
				.andExpect(status().isOk())
				.andExpect(content().string("Visita il comune eseguita con successo"));

		mockMvc.perform(post("/api/animator/add/event")
						.contentType(APPLICATION_JSON)
						.content("""
					 {
						"title": "stelle cadenti",
						"description": "osservazione stelle cadenti",
						"reference": "torre velasca",
						"start": "2025-02-20T20:00:00",
						"end": "2025-02-21T03:00:00"
					}
					"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
	void testGetAllGeoPointUnAuthorizedAdmin() throws Exception {
		mockMvc.perform(get("/api/animator/get/all/geopoint"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
	void testGetAllGeoPointUnAuthorizedCurator() throws Exception {
		mockMvc.perform(get("/api/animator/get/all/geopoint"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetAllGeoPointUnAuthorizedContributor() throws Exception {
		mockMvc.perform(get("/api/animator/get/all/geopoint"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetAllGeoPointUnAuthorizedContributorAuthorized() throws Exception {
		mockMvc.perform(get("/api/animator/get/all/geopoint"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
	void testGetAllGeoPointUnAuthorizedMunicipalityManager() throws Exception {
		mockMvc.perform(get("/api/animator/get/all/geopoint"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator2", idUser = 8,  municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
	void testGetAllGeoPointUnAuthorizedTurist() throws Exception {
		mockMvc.perform(put("/api/user/visited/municipality")
						.param("newMunicipality", "MILANO"))
				.andExpect(status().isOk())
				.andExpect(content().string("Visita il comune eseguita con successo"));

		mockMvc.perform(get("/api/animator/get/all/geopoint"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
	void testGetFinishedContestNoWinnerSuccessful() throws Exception {
		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].id").value(1));
	}

	@Test
	@WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
	void testGetFinishedContestNoWinnerUnauthorizedAdmin() throws Exception {
		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
	void testGetFinishedContestNoWinnerUnauthorizedACurator() throws Exception {
		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetFinishedContestNoWinnerUnauthorizedContributor() throws Exception {
		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetFinishedContestNoWinnerUnauthorizedContributoAuthorized() throws Exception {
		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
	void testGetFinishedContestNoWinnerUnauthorizedMunicipalityManager() throws Exception {
		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
	void testGetFinishedContestNoWinnerUnauthorizedTurist() throws Exception {
		mockMvc.perform(put("/api/user/visited/municipality")
						.param("newMunicipality", "MILANO"))
				.andExpect(status().isOk())
				.andExpect(content().string("Visita il comune eseguita con successo"));

		mockMvc.perform(get("/api/animator/contest/closed"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
	void testGetContestsProgressSuccessful() throws Exception {
		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[1].id").value(2));
	}

	@Test
	@WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
	void testGetContestsProgressUnauthorizedAdmin() throws Exception {
		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
	void testGetContestsProgressUnauthorizedCurator() throws Exception {
		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetContestsProgressUnauthorizedContributor() throws Exception {
		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetContestsProgressUnauthorizedContributorAuthorized() throws Exception {
		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
	void testGetContestsProgressUnauthorizedMunicipalityManager() throws Exception {
		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
	void testGetContestsProgressUnauthorizedTurist() throws Exception {
		mockMvc.perform(put("/api/user/visited/municipality")
						.param("newMunicipality", "MILANO"))
				.andExpect(status().isOk())
				.andExpect(content().string("Visita il comune eseguita con successo"));

		mockMvc.perform(get("/api/animator/contests/progress"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
	void testGetPartecipantSingleContestSuccessful() throws Exception {
		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$.partecipants[0].id").value(4))
				.andExpect(jsonPath("$.partecipants[1].id").value(6));
	}

	@Test
	@WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
	void testGetPartecipantSingleContestUnauthorizedAdmin() throws Exception {
		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
	void testGetPartecipantSingleContestUnauthorizedCurator() throws Exception {
		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetPartecipantSingleContestUnauthorizedContributor() throws Exception {
		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testGetPartecipantSingleContestUnauthorizedContributorAuthorized() throws Exception {
		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
	void testGetPartecipantSingleContestUnauthorizedMunicipalityManager() throws Exception {
		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
	void testGetPartecipantSingleContestUnauthorizedTurist() throws Exception {
		mockMvc.perform(put("/api/user/visited/municipality")
						.param("newMunicipality", "MILANO"))
				.andExpect(status().isOk())
				.andExpect(content().string("Visita il comune eseguita con successo"));

		mockMvc.perform(get("/api/animator/show/partecipants/of/contest")
						.param("idContest", "1"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "animator1", idUser = 4,  municipality = "MILANO", roles = "ANIMATOR", visitedMunicipality = "MILANO")
	void testAssignWinnerSuccessful() throws Exception {
		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isOk())
				.andExpect(content().string("Vincitore assegnato con successo"));
	}

	@Test
	@WithMockUserDetails(username = "admin", idUser = 1,  municipality = "", roles = "ADMIN", visitedMunicipality = "")
	void testAssignWinnerUnauthorizedAdmin() throws Exception {
		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator1", idUser = 3,  municipality = "MILANO", roles = "CURATOR", visitedMunicipality = "MILANO")
	void testAssignWinnerUnauthorizedCurator() throws Exception {
		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "contributor1", idUser = 5,  municipality = "MILANO", roles = "CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testAssignWinnerUnauthorizedContributor() throws Exception {
		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "authorizedContributor1", idUser = 6,  municipality = "MILANO", roles = "AUTHORIZED_CONTRIBUTOR", visitedMunicipality = "MILANO")
	void testAssignWinnerUnauthorizedContributorAuthorized() throws Exception {
		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "manager1", idUser = 2,  municipality = "MILANO", roles = "MUNICIPALITY_MANAGER", visitedMunicipality = "MILANO")
	void testAssignWinnerUnauthorizedMunicipalityManager() throws Exception {
		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

	@Test
	@WithMockUserDetails(username = "curator2", idUser = 8, municipality = "ROMA", roles = "CURATOR", visitedMunicipality = "ROMA")
	void testAssignWinnerUnauthorizedTurist() throws Exception {
		mockMvc.perform(put("/api/user/visited/municipality")
						.param("newMunicipality", "MILANO"))
				.andExpect(status().isOk())
				.andExpect(content().string("Visita il comune eseguita con successo"));

		mockMvc.perform(put("/api/animator/assign/winner")
						.param("idContest", "1")
						.param("idPartecipant", "4"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value("Non hai i permessi per eseguire l'operazione"));
	}

}
