package com.myapp.mongodb.web.rest;

import static com.myapp.mongodb.domain.SocialChargesAsserts.*;
import static com.myapp.mongodb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.mongodb.IntegrationTest;
import com.myapp.mongodb.domain.SocialCharges;
import com.myapp.mongodb.domain.enumeration.SPentType;
import com.myapp.mongodb.domain.enumeration.StatusCharges;
import com.myapp.mongodb.repository.SocialChargesRepository;
import com.myapp.mongodb.service.SocialChargesService;
import com.myapp.mongodb.service.dto.SocialChargesDTO;
import com.myapp.mongodb.service.mapper.SocialChargesMapper;
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
 * Integration tests for the {@link SocialChargesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SocialChargesResourceIT {

    private static final Instant DEFAULT_SPENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SPENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SPentType DEFAULT_SPENT_TYPE = SPentType.MATERIAL;
    private static final SPentType UPDATED_SPENT_TYPE = SPentType.ADMINISTRATIVE;

    private static final StatusCharges DEFAULT_STATUS_CHARGES = StatusCharges.IN_PROGRESS;
    private static final StatusCharges UPDATED_STATUS_CHARGES = StatusCharges.ACCEPTED;

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final String DEFAULT_COMMENT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_TEXT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SocialChargesRepository socialChargesRepository;

    @Mock
    private SocialChargesRepository socialChargesRepositoryMock;

    @Autowired
    private SocialChargesMapper socialChargesMapper;

    @Mock
    private SocialChargesService socialChargesServiceMock;

    @Autowired
    private MockMvc restSocialChargesMockMvc;

    private SocialCharges socialCharges;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialCharges createEntity() {
        SocialCharges socialCharges = new SocialCharges()
            .spentDate(DEFAULT_SPENT_DATE)
            .spentType(DEFAULT_SPENT_TYPE)
            .statusCharges(DEFAULT_STATUS_CHARGES)
            .amount(DEFAULT_AMOUNT)
            .commentText(DEFAULT_COMMENT_TEXT);
        return socialCharges;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialCharges createUpdatedEntity() {
        SocialCharges socialCharges = new SocialCharges()
            .spentDate(UPDATED_SPENT_DATE)
            .spentType(UPDATED_SPENT_TYPE)
            .statusCharges(UPDATED_STATUS_CHARGES)
            .amount(UPDATED_AMOUNT)
            .commentText(UPDATED_COMMENT_TEXT);
        return socialCharges;
    }

    @BeforeEach
    public void initTest() {
        socialChargesRepository.deleteAll();
        socialCharges = createEntity();
    }

    @Test
    void createSocialCharges() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);
        var returnedSocialChargesDTO = om.readValue(
            restSocialChargesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialChargesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SocialChargesDTO.class
        );

        // Validate the SocialCharges in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSocialCharges = socialChargesMapper.toEntity(returnedSocialChargesDTO);
        assertSocialChargesUpdatableFieldsEquals(returnedSocialCharges, getPersistedSocialCharges(returnedSocialCharges));
    }

    @Test
    void createSocialChargesWithExistingId() throws Exception {
        // Create the SocialCharges with an existing ID
        socialCharges.setId("existing_id");
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialChargesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialChargesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        socialCharges.setAmount(null);

        // Create the SocialCharges, which fails.
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        restSocialChargesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialChargesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.save(socialCharges);

        // Get all the socialChargesList
        restSocialChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialCharges.getId())))
            .andExpect(jsonPath("$.[*].spentDate").value(hasItem(DEFAULT_SPENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].spentType").value(hasItem(DEFAULT_SPENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].statusCharges").value(hasItem(DEFAULT_STATUS_CHARGES.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].commentText").value(hasItem(DEFAULT_COMMENT_TEXT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSocialChargesWithEagerRelationshipsIsEnabled() throws Exception {
        when(socialChargesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSocialChargesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(socialChargesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSocialChargesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(socialChargesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSocialChargesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(socialChargesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.save(socialCharges);

        // Get the socialCharges
        restSocialChargesMockMvc
            .perform(get(ENTITY_API_URL_ID, socialCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialCharges.getId()))
            .andExpect(jsonPath("$.spentDate").value(DEFAULT_SPENT_DATE.toString()))
            .andExpect(jsonPath("$.spentType").value(DEFAULT_SPENT_TYPE.toString()))
            .andExpect(jsonPath("$.statusCharges").value(DEFAULT_STATUS_CHARGES.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.commentText").value(DEFAULT_COMMENT_TEXT.toString()));
    }

    @Test
    void getNonExistingSocialCharges() throws Exception {
        // Get the socialCharges
        restSocialChargesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.save(socialCharges);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialCharges
        SocialCharges updatedSocialCharges = socialChargesRepository.findById(socialCharges.getId()).orElseThrow();
        updatedSocialCharges
            .spentDate(UPDATED_SPENT_DATE)
            .spentType(UPDATED_SPENT_TYPE)
            .statusCharges(UPDATED_STATUS_CHARGES)
            .amount(UPDATED_AMOUNT)
            .commentText(UPDATED_COMMENT_TEXT);
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(updatedSocialCharges);

        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialChargesDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSocialChargesToMatchAllProperties(updatedSocialCharges);
    }

    @Test
    void putNonExistingSocialCharges() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialCharges.setId(UUID.randomUUID().toString());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSocialCharges() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialCharges.setId(UUID.randomUUID().toString());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSocialCharges() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialCharges.setId(UUID.randomUUID().toString());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialChargesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSocialChargesWithPatch() throws Exception {
        // Initialize the database
        socialChargesRepository.save(socialCharges);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialCharges using partial update
        SocialCharges partialUpdatedSocialCharges = new SocialCharges();
        partialUpdatedSocialCharges.setId(socialCharges.getId());

        partialUpdatedSocialCharges.statusCharges(UPDATED_STATUS_CHARGES).amount(UPDATED_AMOUNT).commentText(UPDATED_COMMENT_TEXT);

        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSocialCharges))
            )
            .andExpect(status().isOk());

        // Validate the SocialCharges in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialChargesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSocialCharges, socialCharges),
            getPersistedSocialCharges(socialCharges)
        );
    }

    @Test
    void fullUpdateSocialChargesWithPatch() throws Exception {
        // Initialize the database
        socialChargesRepository.save(socialCharges);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialCharges using partial update
        SocialCharges partialUpdatedSocialCharges = new SocialCharges();
        partialUpdatedSocialCharges.setId(socialCharges.getId());

        partialUpdatedSocialCharges
            .spentDate(UPDATED_SPENT_DATE)
            .spentType(UPDATED_SPENT_TYPE)
            .statusCharges(UPDATED_STATUS_CHARGES)
            .amount(UPDATED_AMOUNT)
            .commentText(UPDATED_COMMENT_TEXT);

        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSocialCharges))
            )
            .andExpect(status().isOk());

        // Validate the SocialCharges in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialChargesUpdatableFieldsEquals(partialUpdatedSocialCharges, getPersistedSocialCharges(partialUpdatedSocialCharges));
    }

    @Test
    void patchNonExistingSocialCharges() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialCharges.setId(UUID.randomUUID().toString());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialChargesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSocialCharges() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialCharges.setId(UUID.randomUUID().toString());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(socialChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSocialCharges() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialCharges.setId(UUID.randomUUID().toString());

        // Create the SocialCharges
        SocialChargesDTO socialChargesDTO = socialChargesMapper.toDto(socialCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialChargesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(socialChargesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialCharges in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSocialCharges() throws Exception {
        // Initialize the database
        socialChargesRepository.save(socialCharges);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the socialCharges
        restSocialChargesMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialCharges.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return socialChargesRepository.count();
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

    protected SocialCharges getPersistedSocialCharges(SocialCharges socialCharges) {
        return socialChargesRepository.findById(socialCharges.getId()).orElseThrow();
    }

    protected void assertPersistedSocialChargesToMatchAllProperties(SocialCharges expectedSocialCharges) {
        assertSocialChargesAllPropertiesEquals(expectedSocialCharges, getPersistedSocialCharges(expectedSocialCharges));
    }

    protected void assertPersistedSocialChargesToMatchUpdatableProperties(SocialCharges expectedSocialCharges) {
        assertSocialChargesAllUpdatablePropertiesEquals(expectedSocialCharges, getPersistedSocialCharges(expectedSocialCharges));
    }
}
