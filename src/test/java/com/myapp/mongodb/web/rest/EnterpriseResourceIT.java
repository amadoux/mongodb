package com.myapp.mongodb.web.rest;

import static com.myapp.mongodb.domain.EnterpriseAsserts.*;
import static com.myapp.mongodb.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.mongodb.IntegrationTest;
import com.myapp.mongodb.domain.Enterprise;
import com.myapp.mongodb.domain.enumeration.Pays;
import com.myapp.mongodb.repository.EnterpriseRepository;
import com.myapp.mongodb.service.dto.EnterpriseDTO;
import com.myapp.mongodb.service.mapper.EnterpriseMapper;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link EnterpriseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnterpriseResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_REGISTER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_REGISTER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_UNIQUE_IDENTIFICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_UNIQUE_IDENTIFICATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_DOMICILE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_DOMICILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "=<r0zb@iT.N'E";
    private static final String UPDATED_EMAIL = "53vh[;@d|.xL.B";

    private static final String DEFAULT_BUSINESS_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_PHONE = "BBBBBBBBBB";

    private static final Pays DEFAULT_COUNTRY = Pays.CAMEROON;
    private static final Pays UPDATED_COUNTRY = Pays.SENEGAL;

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_BUSINESS_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BUSINESS_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BUSINESS_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BUSINESS_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_MAP_LOCATOR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_MAP_LOCATOR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_MAP_LOCATOR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_MAP_LOCATOR_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/enterprises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    @Autowired
    private MockMvc restEnterpriseMockMvc;

    private Enterprise enterprise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createEntity() {
        Enterprise enterprise = new Enterprise()
            .companyName(DEFAULT_COMPANY_NAME)
            .businessRegisterNumber(DEFAULT_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(DEFAULT_BUSINESS_DOMICILE)
            .email(DEFAULT_EMAIL)
            .businessPhone(DEFAULT_BUSINESS_PHONE)
            .country(DEFAULT_COUNTRY)
            .city(DEFAULT_CITY)
            .businessLogo(DEFAULT_BUSINESS_LOGO)
            .businessLogoContentType(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(DEFAULT_MAP_LOCATOR)
            .mapLocatorContentType(DEFAULT_MAP_LOCATOR_CONTENT_TYPE);
        return enterprise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enterprise createUpdatedEntity() {
        Enterprise enterprise = new Enterprise()
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(UPDATED_BUSINESS_DOMICILE)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .businessLogo(UPDATED_BUSINESS_LOGO)
            .businessLogoContentType(UPDATED_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(UPDATED_MAP_LOCATOR)
            .mapLocatorContentType(UPDATED_MAP_LOCATOR_CONTENT_TYPE);
        return enterprise;
    }

    @BeforeEach
    public void initTest() {
        enterpriseRepository.deleteAll();
        enterprise = createEntity();
    }

    @Test
    void createEnterprise() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);
        var returnedEnterpriseDTO = om.readValue(
            restEnterpriseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnterpriseDTO.class
        );

        // Validate the Enterprise in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnterprise = enterpriseMapper.toEntity(returnedEnterpriseDTO);
        assertEnterpriseUpdatableFieldsEquals(returnedEnterprise, getPersistedEnterprise(returnedEnterprise));
    }

    @Test
    void createEnterpriseWithExistingId() throws Exception {
        // Create the Enterprise with an existing ID
        enterprise.setId("existing_id");
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCompanyNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enterprise.setCompanyName(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBusinessRegisterNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enterprise.setBusinessRegisterNumber(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkUniqueIdentificationNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enterprise.setUniqueIdentificationNumber(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enterprise.setEmail(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkBusinessPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enterprise.setBusinessPhone(null);

        // Create the Enterprise, which fails.
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        restEnterpriseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllEnterprises() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        // Get all the enterpriseList
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enterprise.getId())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].businessRegisterNumber").value(hasItem(DEFAULT_BUSINESS_REGISTER_NUMBER)))
            .andExpect(jsonPath("$.[*].uniqueIdentificationNumber").value(hasItem(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].businessDomicile").value(hasItem(DEFAULT_BUSINESS_DOMICILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].businessPhone").value(hasItem(DEFAULT_BUSINESS_PHONE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].businessLogoContentType").value(hasItem(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].businessLogo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BUSINESS_LOGO))))
            .andExpect(jsonPath("$.[*].mapLocatorContentType").value(hasItem(DEFAULT_MAP_LOCATOR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].mapLocator").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_MAP_LOCATOR))));
    }

    @Test
    void getEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        // Get the enterprise
        restEnterpriseMockMvc
            .perform(get(ENTITY_API_URL_ID, enterprise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enterprise.getId()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.businessRegisterNumber").value(DEFAULT_BUSINESS_REGISTER_NUMBER))
            .andExpect(jsonPath("$.uniqueIdentificationNumber").value(DEFAULT_UNIQUE_IDENTIFICATION_NUMBER))
            .andExpect(jsonPath("$.businessDomicile").value(DEFAULT_BUSINESS_DOMICILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.businessPhone").value(DEFAULT_BUSINESS_PHONE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.businessLogoContentType").value(DEFAULT_BUSINESS_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.businessLogo").value(Base64.getEncoder().encodeToString(DEFAULT_BUSINESS_LOGO)))
            .andExpect(jsonPath("$.mapLocatorContentType").value(DEFAULT_MAP_LOCATOR_CONTENT_TYPE))
            .andExpect(jsonPath("$.mapLocator").value(Base64.getEncoder().encodeToString(DEFAULT_MAP_LOCATOR)));
    }

    @Test
    void getNonExistingEnterprise() throws Exception {
        // Get the enterprise
        restEnterpriseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enterprise
        Enterprise updatedEnterprise = enterpriseRepository.findById(enterprise.getId()).orElseThrow();
        updatedEnterprise
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(UPDATED_BUSINESS_DOMICILE)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .businessLogo(UPDATED_BUSINESS_LOGO)
            .businessLogoContentType(UPDATED_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(UPDATED_MAP_LOCATOR)
            .mapLocatorContentType(UPDATED_MAP_LOCATOR_CONTENT_TYPE);
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(updatedEnterprise);

        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enterpriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enterpriseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnterpriseToMatchAllProperties(updatedEnterprise);
    }

    @Test
    void putNonExistingEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(UUID.randomUUID().toString());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enterpriseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(UUID.randomUUID().toString());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(UUID.randomUUID().toString());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnterpriseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnterprise, enterprise),
            getPersistedEnterprise(enterprise)
        );
    }

    @Test
    void fullUpdateEnterpriseWithPatch() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enterprise using partial update
        Enterprise partialUpdatedEnterprise = new Enterprise();
        partialUpdatedEnterprise.setId(enterprise.getId());

        partialUpdatedEnterprise
            .companyName(UPDATED_COMPANY_NAME)
            .businessRegisterNumber(UPDATED_BUSINESS_REGISTER_NUMBER)
            .uniqueIdentificationNumber(UPDATED_UNIQUE_IDENTIFICATION_NUMBER)
            .businessDomicile(UPDATED_BUSINESS_DOMICILE)
            .email(UPDATED_EMAIL)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .country(UPDATED_COUNTRY)
            .city(UPDATED_CITY)
            .businessLogo(UPDATED_BUSINESS_LOGO)
            .businessLogoContentType(UPDATED_BUSINESS_LOGO_CONTENT_TYPE)
            .mapLocator(UPDATED_MAP_LOCATOR)
            .mapLocatorContentType(UPDATED_MAP_LOCATOR_CONTENT_TYPE);

        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnterprise.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnterprise))
            )
            .andExpect(status().isOk());

        // Validate the Enterprise in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnterpriseUpdatableFieldsEquals(partialUpdatedEnterprise, getPersistedEnterprise(partialUpdatedEnterprise));
    }

    @Test
    void patchNonExistingEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(UUID.randomUUID().toString());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enterpriseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(UUID.randomUUID().toString());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enterpriseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEnterprise() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enterprise.setId(UUID.randomUUID().toString());

        // Create the Enterprise
        EnterpriseDTO enterpriseDTO = enterpriseMapper.toDto(enterprise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnterpriseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enterpriseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enterprise in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEnterprise() throws Exception {
        // Initialize the database
        enterpriseRepository.save(enterprise);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enterprise
        restEnterpriseMockMvc
            .perform(delete(ENTITY_API_URL_ID, enterprise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enterpriseRepository.count();
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

    protected Enterprise getPersistedEnterprise(Enterprise enterprise) {
        return enterpriseRepository.findById(enterprise.getId()).orElseThrow();
    }

    protected void assertPersistedEnterpriseToMatchAllProperties(Enterprise expectedEnterprise) {
        assertEnterpriseAllPropertiesEquals(expectedEnterprise, getPersistedEnterprise(expectedEnterprise));
    }

    protected void assertPersistedEnterpriseToMatchUpdatableProperties(Enterprise expectedEnterprise) {
        assertEnterpriseAllUpdatablePropertiesEquals(expectedEnterprise, getPersistedEnterprise(expectedEnterprise));
    }
}
