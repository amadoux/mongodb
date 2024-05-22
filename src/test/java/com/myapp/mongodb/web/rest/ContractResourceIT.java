package com.myapp.mongodb.web.rest;

import static com.myapp.mongodb.domain.ContractAsserts.*;
import static com.myapp.mongodb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.mongodb.IntegrationTest;
import com.myapp.mongodb.domain.Contract;
import com.myapp.mongodb.domain.enumeration.ContractType;
import com.myapp.mongodb.domain.enumeration.StatusContract;
import com.myapp.mongodb.repository.ContractRepository;
import com.myapp.mongodb.service.ContractService;
import com.myapp.mongodb.service.dto.ContractDTO;
import com.myapp.mongodb.service.mapper.ContractMapper;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.CDD;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.CDI;

    private static final Instant DEFAULT_ENTRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StatusContract DEFAULT_STATUS_CONTRACT = StatusContract.ENCOURS;
    private static final StatusContract UPDATED_STATUS_CONTRACT = StatusContract.RUPTURE_CONVENTIONNELLE;

    private static final byte[] DEFAULT_UPLOAD_CONTRACT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_UPLOAD_CONTRACT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractRepository contractRepository;

    @Mock
    private ContractRepository contractRepositoryMock;

    @Autowired
    private ContractMapper contractMapper;

    @Mock
    private ContractService contractServiceMock;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity() {
        Contract contract = new Contract()
            .contractType(DEFAULT_CONTRACT_TYPE)
            .entryDate(DEFAULT_ENTRY_DATE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .statusContract(DEFAULT_STATUS_CONTRACT)
            .uploadContract(DEFAULT_UPLOAD_CONTRACT)
            .uploadContractContentType(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity() {
        Contract contract = new Contract()
            .contractType(UPDATED_CONTRACT_TYPE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contractRepository.deleteAll();
        contract = createEntity();
    }

    @Test
    void createContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        var returnedContractDTO = om.readValue(
            restContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractDTO.class
        );

        // Validate the Contract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContract = contractMapper.toEntity(returnedContractDTO);
        assertContractUpdatableFieldsEquals(returnedContract, getPersistedContract(returnedContract));
    }

    @Test
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId("existing_id");
        ContractDTO contractDTO = contractMapper.toDto(contract);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.save(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].statusContract").value(hasItem(DEFAULT_STATUS_CONTRACT.toString())))
            .andExpect(jsonPath("$.[*].uploadContractContentType").value(hasItem(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].uploadContract").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_CONTRACT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contractRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.save(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId()))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.statusContract").value(DEFAULT_STATUS_CONTRACT.toString()))
            .andExpect(jsonPath("$.uploadContractContentType").value(DEFAULT_UPLOAD_CONTRACT_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadContract").value(Base64.getEncoder().encodeToString(DEFAULT_UPLOAD_CONTRACT)));
    }

    @Test
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.save(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        updatedContract
            .contractType(UPDATED_CONTRACT_TYPE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractToMatchAllProperties(updatedContract);
    }

    @Test
    void putNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(UUID.randomUUID().toString());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(UUID.randomUUID().toString());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(UUID.randomUUID().toString());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.save(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContract, contract), getPersistedContract(contract));
    }

    @Test
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.save(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .contractType(UPDATED_CONTRACT_TYPE)
            .entryDate(UPDATED_ENTRY_DATE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .statusContract(UPDATED_STATUS_CONTRACT)
            .uploadContract(UPDATED_UPLOAD_CONTRACT)
            .uploadContractContentType(UPDATED_UPLOAD_CONTRACT_CONTENT_TYPE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(partialUpdatedContract, getPersistedContract(partialUpdatedContract));
    }

    @Test
    void patchNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(UUID.randomUUID().toString());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(UUID.randomUUID().toString());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contract.setId(UUID.randomUUID().toString());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.save(contract);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractRepository.count();
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

    protected Contract getPersistedContract(Contract contract) {
        return contractRepository.findById(contract.getId()).orElseThrow();
    }

    protected void assertPersistedContractToMatchAllProperties(Contract expectedContract) {
        assertContractAllPropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }

    protected void assertPersistedContractToMatchUpdatableProperties(Contract expectedContract) {
        assertContractAllUpdatablePropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }
}
