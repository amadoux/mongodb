package com.myapp.mongodb.web.rest;

import static com.myapp.mongodb.domain.AbsenceAsserts.*;
import static com.myapp.mongodb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.mongodb.IntegrationTest;
import com.myapp.mongodb.domain.Absence;
import com.myapp.mongodb.domain.enumeration.ConfirmationAbsence;
import com.myapp.mongodb.domain.enumeration.TypeAbsence;
import com.myapp.mongodb.repository.AbsenceRepository;
import com.myapp.mongodb.service.AbsenceService;
import com.myapp.mongodb.service.dto.AbsenceDTO;
import com.myapp.mongodb.service.mapper.AbsenceMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link AbsenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AbsenceResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_NUMBER_DAY_ABSENCE = 1L;
    private static final Long UPDATED_NUMBER_DAY_ABSENCE = 2L;

    private static final TypeAbsence DEFAULT_TYPE_ABSENCE = TypeAbsence.RTT;
    private static final TypeAbsence UPDATED_TYPE_ABSENCE = TypeAbsence.CONGES_PAYES;

    private static final ConfirmationAbsence DEFAULT_CONFIRMATION_ABSENCE = ConfirmationAbsence.ENCOURS;
    private static final ConfirmationAbsence UPDATED_CONFIRMATION_ABSENCE = ConfirmationAbsence.REJETE;

    private static final Long DEFAULT_CONGE_RESTANT = 1L;
    private static final Long UPDATED_CONGE_RESTANT = 2L;

    private static final String ENTITY_API_URL = "/api/absences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AbsenceRepository absenceRepository;

    @Mock
    private AbsenceRepository absenceRepositoryMock;

    @Autowired
    private AbsenceMapper absenceMapper;

    @Mock
    private AbsenceService absenceServiceMock;

    @Autowired
    private MockMvc restAbsenceMockMvc;

    private Absence absence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absence createEntity() {
        Absence absence = new Absence()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .numberDayAbsence(DEFAULT_NUMBER_DAY_ABSENCE)
            .typeAbsence(DEFAULT_TYPE_ABSENCE)
            .confirmationAbsence(DEFAULT_CONFIRMATION_ABSENCE)
            .congeRestant(DEFAULT_CONGE_RESTANT);
        return absence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Absence createUpdatedEntity() {
        Absence absence = new Absence()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .confirmationAbsence(UPDATED_CONFIRMATION_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);
        return absence;
    }

    @BeforeEach
    public void initTest() {
        absenceRepository.deleteAll();
        absence = createEntity();
    }

    @Test
    void createAbsence() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);
        var returnedAbsenceDTO = om.readValue(
            restAbsenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absenceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AbsenceDTO.class
        );

        // Validate the Absence in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAbsence = absenceMapper.toEntity(returnedAbsenceDTO);
        assertAbsenceUpdatableFieldsEquals(returnedAbsence, getPersistedAbsence(returnedAbsence));
    }

    @Test
    void createAbsenceWithExistingId() throws Exception {
        // Create the Absence with an existing ID
        absence.setId("existing_id");
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbsenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAbsences() throws Exception {
        // Initialize the database
        absenceRepository.save(absence);

        // Get all the absenceList
        restAbsenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(absence.getId())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberDayAbsence").value(hasItem(DEFAULT_NUMBER_DAY_ABSENCE.intValue())))
            .andExpect(jsonPath("$.[*].typeAbsence").value(hasItem(DEFAULT_TYPE_ABSENCE.toString())))
            .andExpect(jsonPath("$.[*].confirmationAbsence").value(hasItem(DEFAULT_CONFIRMATION_ABSENCE.toString())))
            .andExpect(jsonPath("$.[*].congeRestant").value(hasItem(DEFAULT_CONGE_RESTANT.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(absenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(absenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbsencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(absenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbsenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(absenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getAbsence() throws Exception {
        // Initialize the database
        absenceRepository.save(absence);

        // Get the absence
        restAbsenceMockMvc
            .perform(get(ENTITY_API_URL_ID, absence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(absence.getId()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.numberDayAbsence").value(DEFAULT_NUMBER_DAY_ABSENCE.intValue()))
            .andExpect(jsonPath("$.typeAbsence").value(DEFAULT_TYPE_ABSENCE.toString()))
            .andExpect(jsonPath("$.confirmationAbsence").value(DEFAULT_CONFIRMATION_ABSENCE.toString()))
            .andExpect(jsonPath("$.congeRestant").value(DEFAULT_CONGE_RESTANT.intValue()));
    }

    @Test
    void getNonExistingAbsence() throws Exception {
        // Get the absence
        restAbsenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAbsence() throws Exception {
        // Initialize the database
        absenceRepository.save(absence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the absence
        Absence updatedAbsence = absenceRepository.findById(absence.getId()).orElseThrow();
        updatedAbsence
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .confirmationAbsence(UPDATED_CONFIRMATION_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);
        AbsenceDTO absenceDTO = absenceMapper.toDto(updatedAbsence);

        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absenceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAbsenceToMatchAllProperties(updatedAbsence);
    }

    @Test
    void putNonExistingAbsence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absence.setId(UUID.randomUUID().toString());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, absenceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAbsence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absence.setId(UUID.randomUUID().toString());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAbsence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absence.setId(UUID.randomUUID().toString());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(absenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAbsenceWithPatch() throws Exception {
        // Initialize the database
        absenceRepository.save(absence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the absence using partial update
        Absence partialUpdatedAbsence = new Absence();
        partialUpdatedAbsence.setId(absence.getId());

        partialUpdatedAbsence.numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE);

        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAbsence))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAbsenceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAbsence, absence), getPersistedAbsence(absence));
    }

    @Test
    void fullUpdateAbsenceWithPatch() throws Exception {
        // Initialize the database
        absenceRepository.save(absence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the absence using partial update
        Absence partialUpdatedAbsence = new Absence();
        partialUpdatedAbsence.setId(absence.getId());

        partialUpdatedAbsence
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .numberDayAbsence(UPDATED_NUMBER_DAY_ABSENCE)
            .typeAbsence(UPDATED_TYPE_ABSENCE)
            .confirmationAbsence(UPDATED_CONFIRMATION_ABSENCE)
            .congeRestant(UPDATED_CONGE_RESTANT);

        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbsence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAbsence))
            )
            .andExpect(status().isOk());

        // Validate the Absence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAbsenceUpdatableFieldsEquals(partialUpdatedAbsence, getPersistedAbsence(partialUpdatedAbsence));
    }

    @Test
    void patchNonExistingAbsence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absence.setId(UUID.randomUUID().toString());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, absenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAbsence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absence.setId(UUID.randomUUID().toString());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(absenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAbsence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        absence.setId(UUID.randomUUID().toString());

        // Create the Absence
        AbsenceDTO absenceDTO = absenceMapper.toDto(absence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbsenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(absenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Absence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAbsence() throws Exception {
        // Initialize the database
        absenceRepository.save(absence);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the absence
        restAbsenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, absence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return absenceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Absence getPersistedAbsence(Absence absence) {
        return absenceRepository.findById(absence.getId()).orElseThrow();
    }

    protected void assertPersistedAbsenceToMatchAllProperties(Absence expectedAbsence) {
        assertAbsenceAllPropertiesEquals(expectedAbsence, getPersistedAbsence(expectedAbsence));
    }

    protected void assertPersistedAbsenceToMatchUpdatableProperties(Absence expectedAbsence) {
        assertAbsenceAllUpdatablePropertiesEquals(expectedAbsence, getPersistedAbsence(expectedAbsence));
    }
}
