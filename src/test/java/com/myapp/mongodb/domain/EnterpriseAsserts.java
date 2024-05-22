package com.myapp.mongodb.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class EnterpriseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseAllPropertiesEquals(Enterprise expected, Enterprise actual) {
        assertEnterpriseAutoGeneratedPropertiesEquals(expected, actual);
        assertEnterpriseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseAllUpdatablePropertiesEquals(Enterprise expected, Enterprise actual) {
        assertEnterpriseUpdatableFieldsEquals(expected, actual);
        assertEnterpriseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseAutoGeneratedPropertiesEquals(Enterprise expected, Enterprise actual) {
        assertThat(expected)
            .as("Verify Enterprise auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseUpdatableFieldsEquals(Enterprise expected, Enterprise actual) {
        assertThat(expected)
            .as("Verify Enterprise relevant properties")
            .satisfies(e -> assertThat(e.getCompanyName()).as("check companyName").isEqualTo(actual.getCompanyName()))
            .satisfies(
                e ->
                    assertThat(e.getBusinessRegisterNumber())
                        .as("check businessRegisterNumber")
                        .isEqualTo(actual.getBusinessRegisterNumber())
            )
            .satisfies(
                e ->
                    assertThat(e.getUniqueIdentificationNumber())
                        .as("check uniqueIdentificationNumber")
                        .isEqualTo(actual.getUniqueIdentificationNumber())
            )
            .satisfies(e -> assertThat(e.getBusinessDomicile()).as("check businessDomicile").isEqualTo(actual.getBusinessDomicile()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getBusinessPhone()).as("check businessPhone").isEqualTo(actual.getBusinessPhone()))
            .satisfies(e -> assertThat(e.getCountry()).as("check country").isEqualTo(actual.getCountry()))
            .satisfies(e -> assertThat(e.getCity()).as("check city").isEqualTo(actual.getCity()))
            .satisfies(e -> assertThat(e.getBusinessLogo()).as("check businessLogo").isEqualTo(actual.getBusinessLogo()))
            .satisfies(
                e ->
                    assertThat(e.getBusinessLogoContentType())
                        .as("check businessLogo contenty type")
                        .isEqualTo(actual.getBusinessLogoContentType())
            )
            .satisfies(e -> assertThat(e.getMapLocator()).as("check mapLocator").isEqualTo(actual.getMapLocator()))
            .satisfies(
                e ->
                    assertThat(e.getMapLocatorContentType())
                        .as("check mapLocator contenty type")
                        .isEqualTo(actual.getMapLocatorContentType())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertEnterpriseUpdatableRelationshipsEquals(Enterprise expected, Enterprise actual) {}
}
