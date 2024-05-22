package com.myapp.mongodb.service.dto;

import com.myapp.mongodb.domain.enumeration.ContractType;
import com.myapp.mongodb.domain.enumeration.Department;
import com.myapp.mongodb.domain.enumeration.Level;
import com.myapp.mongodb.domain.enumeration.Pays;
import com.myapp.mongodb.domain.enumeration.SalaryType;
import com.myapp.mongodb.domain.enumeration.TypeEmployed;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.mongodb.domain.Employee} entity.
 */
@Schema(description = "The Employee entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeDTO implements Serializable {

    private String id;

    private String firstName;

    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String identityCard;

    private Instant dateInspiration;

    private Pays nationality;

    private byte[] uploadIdentityCard;

    private String uploadIdentityCardContentType;

    private TypeEmployed typeEmployed;

    private String cityAgency;

    private String residenceCity;

    private String address;

    private String socialSecurityNumber;

    private Instant birthDate;

    private String birthPlace;

    private Instant entryDate;

    private Instant releaseDate;

    private String workstation;

    private String descriptionWorkstation;

    private Department department;

    private Level level;

    private Long coefficient;

    private String numberHours;

    private String averageHourlyCost;

    private Long monthlyGrossAmount;

    private Long commissionAmount;

    private ContractType contractType;

    private SalaryType salaryType;

    private Instant hireDate;

    private EnterpriseDTO enterprise;

    private EmployeeDTO managerEmployee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Instant getDateInspiration() {
        return dateInspiration;
    }

    public void setDateInspiration(Instant dateInspiration) {
        this.dateInspiration = dateInspiration;
    }

    public Pays getNationality() {
        return nationality;
    }

    public void setNationality(Pays nationality) {
        this.nationality = nationality;
    }

    public byte[] getUploadIdentityCard() {
        return uploadIdentityCard;
    }

    public void setUploadIdentityCard(byte[] uploadIdentityCard) {
        this.uploadIdentityCard = uploadIdentityCard;
    }

    public String getUploadIdentityCardContentType() {
        return uploadIdentityCardContentType;
    }

    public void setUploadIdentityCardContentType(String uploadIdentityCardContentType) {
        this.uploadIdentityCardContentType = uploadIdentityCardContentType;
    }

    public TypeEmployed getTypeEmployed() {
        return typeEmployed;
    }

    public void setTypeEmployed(TypeEmployed typeEmployed) {
        this.typeEmployed = typeEmployed;
    }

    public String getCityAgency() {
        return cityAgency;
    }

    public void setCityAgency(String cityAgency) {
        this.cityAgency = cityAgency;
    }

    public String getResidenceCity() {
        return residenceCity;
    }

    public void setResidenceCity(String residenceCity) {
        this.residenceCity = residenceCity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Instant getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getDescriptionWorkstation() {
        return descriptionWorkstation;
    }

    public void setDescriptionWorkstation(String descriptionWorkstation) {
        this.descriptionWorkstation = descriptionWorkstation;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Long getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Long coefficient) {
        this.coefficient = coefficient;
    }

    public String getNumberHours() {
        return numberHours;
    }

    public void setNumberHours(String numberHours) {
        this.numberHours = numberHours;
    }

    public String getAverageHourlyCost() {
        return averageHourlyCost;
    }

    public void setAverageHourlyCost(String averageHourlyCost) {
        this.averageHourlyCost = averageHourlyCost;
    }

    public Long getMonthlyGrossAmount() {
        return monthlyGrossAmount;
    }

    public void setMonthlyGrossAmount(Long monthlyGrossAmount) {
        this.monthlyGrossAmount = monthlyGrossAmount;
    }

    public Long getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Long commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public SalaryType getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(SalaryType salaryType) {
        this.salaryType = salaryType;
    }

    public Instant getHireDate() {
        return hireDate;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public EnterpriseDTO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseDTO enterprise) {
        this.enterprise = enterprise;
    }

    public EmployeeDTO getManagerEmployee() {
        return managerEmployee;
    }

    public void setManagerEmployee(EmployeeDTO managerEmployee) {
        this.managerEmployee = managerEmployee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id='" + getId() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", identityCard='" + getIdentityCard() + "'" +
            ", dateInspiration='" + getDateInspiration() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", uploadIdentityCard='" + getUploadIdentityCard() + "'" +
            ", typeEmployed='" + getTypeEmployed() + "'" +
            ", cityAgency='" + getCityAgency() + "'" +
            ", residenceCity='" + getResidenceCity() + "'" +
            ", address='" + getAddress() + "'" +
            ", socialSecurityNumber='" + getSocialSecurityNumber() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", workstation='" + getWorkstation() + "'" +
            ", descriptionWorkstation='" + getDescriptionWorkstation() + "'" +
            ", department='" + getDepartment() + "'" +
            ", level='" + getLevel() + "'" +
            ", coefficient=" + getCoefficient() +
            ", numberHours='" + getNumberHours() + "'" +
            ", averageHourlyCost='" + getAverageHourlyCost() + "'" +
            ", monthlyGrossAmount=" + getMonthlyGrossAmount() +
            ", commissionAmount=" + getCommissionAmount() +
            ", contractType='" + getContractType() + "'" +
            ", salaryType='" + getSalaryType() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            ", enterprise=" + getEnterprise() +
            ", managerEmployee=" + getManagerEmployee() +
            "}";
    }
}
