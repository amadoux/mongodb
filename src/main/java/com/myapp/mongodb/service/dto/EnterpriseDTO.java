package com.myapp.mongodb.service.dto;

import com.myapp.mongodb.domain.enumeration.Pays;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.mongodb.domain.Enterprise} entity.
 */
@Schema(description = "Entreprise")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnterpriseDTO implements Serializable {

    private String id;

    @NotNull
    @Schema(description = "fieldName", required = true)
    private String companyName;

    @NotNull
    private String businessRegisterNumber;

    @NotNull
    private String uniqueIdentificationNumber;

    private String businessDomicile;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @NotNull
    private String businessPhone;

    private Pays country;

    private String city;

    private byte[] businessLogo;

    private String businessLogoContentType;

    private byte[] mapLocator;

    private String mapLocatorContentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessRegisterNumber() {
        return businessRegisterNumber;
    }

    public void setBusinessRegisterNumber(String businessRegisterNumber) {
        this.businessRegisterNumber = businessRegisterNumber;
    }

    public String getUniqueIdentificationNumber() {
        return uniqueIdentificationNumber;
    }

    public void setUniqueIdentificationNumber(String uniqueIdentificationNumber) {
        this.uniqueIdentificationNumber = uniqueIdentificationNumber;
    }

    public String getBusinessDomicile() {
        return businessDomicile;
    }

    public void setBusinessDomicile(String businessDomicile) {
        this.businessDomicile = businessDomicile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public Pays getCountry() {
        return country;
    }

    public void setCountry(Pays country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(byte[] businessLogo) {
        this.businessLogo = businessLogo;
    }

    public String getBusinessLogoContentType() {
        return businessLogoContentType;
    }

    public void setBusinessLogoContentType(String businessLogoContentType) {
        this.businessLogoContentType = businessLogoContentType;
    }

    public byte[] getMapLocator() {
        return mapLocator;
    }

    public void setMapLocator(byte[] mapLocator) {
        this.mapLocator = mapLocator;
    }

    public String getMapLocatorContentType() {
        return mapLocatorContentType;
    }

    public void setMapLocatorContentType(String mapLocatorContentType) {
        this.mapLocatorContentType = mapLocatorContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnterpriseDTO)) {
            return false;
        }

        EnterpriseDTO enterpriseDTO = (EnterpriseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enterpriseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnterpriseDTO{" +
            "id='" + getId() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", businessRegisterNumber='" + getBusinessRegisterNumber() + "'" +
            ", uniqueIdentificationNumber='" + getUniqueIdentificationNumber() + "'" +
            ", businessDomicile='" + getBusinessDomicile() + "'" +
            ", email='" + getEmail() + "'" +
            ", businessPhone='" + getBusinessPhone() + "'" +
            ", country='" + getCountry() + "'" +
            ", city='" + getCity() + "'" +
            ", businessLogo='" + getBusinessLogo() + "'" +
            ", mapLocator='" + getMapLocator() + "'" +
            "}";
    }
}
