package com.myapp.mongodb.web.rest;

import static com.myapp.mongodb.domain.PaySlipAsserts.*;
import static com.myapp.mongodb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.mongodb.IntegrationTest;
import com.myapp.mongodb.domain.PaySlip;
import com.myapp.mongodb.repository.PaySlipRepository;
import com.myapp.mongodb.service.PaySlipService;
import com.myapp.mongodb.service.dto.PaySlipDTO;
import com.myapp.mongodb.service.mapper.PaySlipMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link PaySlipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PaySlipResourceIT {

    private static final Long DEFAULT_NET_SALARY_PAY = 1L;
    private static final Long UPDATED_NET_SALARY_PAY = 2L;

    private static final Instant DEFAULT_PAY_SLIP_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_SLIP_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_UPLOAD_PAY_SLIP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_UPLOAD_PAY_SLIP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/pay-slips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaySlipRepository paySlipRepository;

    @Mock
    private PaySlipRepository paySlipRepositoryMock;

    @Autowired
    private PaySlipMapper paySlipMapper;

    @Mock
    private PaySlipService paySlipServiceMock;

    @Autowired
    private MockMvc restPaySlipMockMvc;

    private PaySlip paySlip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaySlip createEntity() {
        PaySlip paySlip = new PaySlip()
            .netSalaryPay(DEFAULT_NET_SALARY_PAY)
            .paySlipDate(DEFAULT_PAY_SLIP_DATE)
            .uploadPaySlip(DEFAULT_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE);
        return paySlip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaySlip createUpdatedEntity() {
        PaySlip paySlip = new PaySlip()
            .netSalaryPay(UPDATED_NET_SALARY_PAY)
            .paySlipDate(UPDATED_PAY_SLIP_DATE)
            .uploadPaySlip(UPDATED_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);
        return paySlip;
    }

    @BeforeEach
    public void initTest() {
        paySlipRepository.deleteAll();
        paySlip = createEntity();
    }

    @Test
    void createPaySlip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);
        var returnedPaySlipDTO = om.readValue(
            restPaySlipMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlipDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaySlipDTO.class
        );

        // Validate the PaySlip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPaySlip = paySlipMapper.toEntity(returnedPaySlipDTO);
        assertPaySlipUpdatableFieldsEquals(returnedPaySlip, getPersistedPaySlip(returnedPaySlip));
    }

    @Test
    void createPaySlipWithExistingId() throws Exception {
        // Create the PaySlip with an existing ID
        paySlip.setId("existing_id");
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaySlipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPaySlips() throws Exception {
        // Initialize the database
        paySlipRepository.save(paySlip);

        // Get all the paySlipList
        restPaySlipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paySlip.getId())))
            .andExpect(jsonPath("$.[*].netSalaryPay").value(hasItem(DEFAULT_NET_SALARY_PAY.intValue())))
            .andExpect(jsonPath("$.[*].paySlipDate").value(hasItem(DEFAULT_PAY_SLIP_DATE.toString())))
            .andExpect(jsonPath("$.[*].uploadPaySlipContentType").value(hasItem(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].uploadPaySlip").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_PAY_SLIP))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaySlipsWithEagerRelationshipsIsEnabled() throws Exception {
        when(paySlipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaySlipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(paySlipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaySlipsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(paySlipServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPaySlipMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(paySlipRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getPaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.save(paySlip);

        // Get the paySlip
        restPaySlipMockMvc
            .perform(get(ENTITY_API_URL_ID, paySlip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paySlip.getId()))
            .andExpect(jsonPath("$.netSalaryPay").value(DEFAULT_NET_SALARY_PAY.intValue()))
            .andExpect(jsonPath("$.paySlipDate").value(DEFAULT_PAY_SLIP_DATE.toString()))
            .andExpect(jsonPath("$.uploadPaySlipContentType").value(DEFAULT_UPLOAD_PAY_SLIP_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadPaySlip").value(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_PAY_SLIP)));
    }

    @Test
    void getNonExistingPaySlip() throws Exception {
        // Get the paySlip
        restPaySlipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.save(paySlip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paySlip
        PaySlip updatedPaySlip = paySlipRepository.findById(paySlip.getId()).orElseThrow();
        updatedPaySlip
            .netSalaryPay(UPDATED_NET_SALARY_PAY)
            .paySlipDate(UPDATED_PAY_SLIP_DATE)
            .uploadPaySlip(UPDATED_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(updatedPaySlip);

        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paySlipDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlipDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaySlipToMatchAllProperties(updatedPaySlip);
    }

    @Test
    void putNonExistingPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(UUID.randomUUID().toString());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paySlipDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(UUID.randomUUID().toString());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(UUID.randomUUID().toString());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paySlipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaySlipWithPatch() throws Exception {
        // Initialize the database
        paySlipRepository.save(paySlip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paySlip using partial update
        PaySlip partialUpdatedPaySlip = new PaySlip();
        partialUpdatedPaySlip.setId(paySlip.getId());

        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaySlip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaySlipUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPaySlip, paySlip), getPersistedPaySlip(paySlip));
    }

    @Test
    void fullUpdatePaySlipWithPatch() throws Exception {
        // Initialize the database
        paySlipRepository.save(paySlip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the paySlip using partial update
        PaySlip partialUpdatedPaySlip = new PaySlip();
        partialUpdatedPaySlip.setId(paySlip.getId());

        partialUpdatedPaySlip
            .netSalaryPay(UPDATED_NET_SALARY_PAY)
            .paySlipDate(UPDATED_PAY_SLIP_DATE)
            .uploadPaySlip(UPDATED_UPLOAD_PAY_SLIP)
            .uploadPaySlipContentType(UPDATED_UPLOAD_PAY_SLIP_CONTENT_TYPE);

        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaySlip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPaySlip))
            )
            .andExpect(status().isOk());

        // Validate the PaySlip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaySlipUpdatableFieldsEquals(partialUpdatedPaySlip, getPersistedPaySlip(partialUpdatedPaySlip));
    }

    @Test
    void patchNonExistingPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(UUID.randomUUID().toString());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paySlipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(UUID.randomUUID().toString());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paySlipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPaySlip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        paySlip.setId(UUID.randomUUID().toString());

        // Create the PaySlip
        PaySlipDTO paySlipDTO = paySlipMapper.toDto(paySlip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaySlipMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paySlipDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaySlip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePaySlip() throws Exception {
        // Initialize the database
        paySlipRepository.save(paySlip);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the paySlip
        restPaySlipMockMvc
            .perform(delete(ENTITY_API_URL_ID, paySlip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paySlipRepository.count();
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

    protected PaySlip getPersistedPaySlip(PaySlip paySlip) {
        return paySlipRepository.findById(paySlip.getId()).orElseThrow();
    }

    protected void assertPersistedPaySlipToMatchAllProperties(PaySlip expectedPaySlip) {
        assertPaySlipAllPropertiesEquals(expectedPaySlip, getPersistedPaySlip(expectedPaySlip));
    }

    protected void assertPersistedPaySlipToMatchUpdatableProperties(PaySlip expectedPaySlip) {
        assertPaySlipAllUpdatablePropertiesEquals(expectedPaySlip, getPersistedPaySlip(expectedPaySlip));
    }
}
