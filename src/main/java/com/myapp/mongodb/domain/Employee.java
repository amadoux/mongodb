package com.myapp.mongodb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.mongodb.domain.enumeration.ContractType;
import com.myapp.mongodb.domain.enumeration.Department;
import com.myapp.mongodb.domain.enumeration.Level;
import com.myapp.mongodb.domain.enumeration.Pays;
import com.myapp.mongodb.domain.enumeration.SalaryType;
import com.myapp.mongodb.domain.enumeration.TypeEmployed;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The Employee entity.
 */
@Document(collection = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("first_name")
    private String firstName;

    @Field("last_name")
    private String lastName;

    @NotNull
    @Field("email")
    private String email;

    @NotNull
    @Field("phone_number")
    private String phoneNumber;

    @NotNull
    @Field("identity_card")
    private String identityCard;

    @Field("date_inspiration")
    private Instant dateInspiration;

    @Field("nationality")
    private Pays nationality;

    @Field("upload_identity_card")
    private byte[] uploadIdentityCard;

    @Field("upload_identity_card_content_type")
    private String uploadIdentityCardContentType;

    @Field("type_employed")
    private TypeEmployed typeEmployed;

    @Field("city_agency")
    private String cityAgency;

    @Field("residence_city")
    private String residenceCity;

    @Field("address")
    private String address;

    @Field("social_security_number")
    private String socialSecurityNumber;

    @Field("birth_date")
    private Instant birthDate;

    @Field("birth_place")
    private String birthPlace;

    @Field("entry_date")
    private Instant entryDate;

    @Field("release_date")
    private Instant releaseDate;

    @Field("workstation")
    private String workstation;

    @Field("description_workstation")
    private String descriptionWorkstation;

    @Field("department")
    private Department department;

    @Field("level")
    private Level level;

    @Field("coefficient")
    private Long coefficient;

    @Field("number_hours")
    private String numberHours;

    @Field("average_hourly_cost")
    private String averageHourlyCost;

    @Field("monthly_gross_amount")
    private Long monthlyGrossAmount;

    @Field("commission_amount")
    private Long commissionAmount;

    @Field("contract_type")
    private ContractType contractType;

    @Field("salary_type")
    private SalaryType salaryType;

    @Field("hire_date")
    private Instant hireDate;

    @DBRef
    @Field("enterprise")
    private Enterprise enterprise;

    @DBRef
    @Field("managerEmployee")
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Employee managerEmployee;

    @DBRef
    @Field("manager")
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Set<Employee> managers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Employee id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Employee phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public Employee identityCard(String identityCard) {
        this.setIdentityCard(identityCard);
        return this;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Instant getDateInspiration() {
        return this.dateInspiration;
    }

    public Employee dateInspiration(Instant dateInspiration) {
        this.setDateInspiration(dateInspiration);
        return this;
    }

    public void setDateInspiration(Instant dateInspiration) {
        this.dateInspiration = dateInspiration;
    }

    public Pays getNationality() {
        return this.nationality;
    }

    public Employee nationality(Pays nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(Pays nationality) {
        this.nationality = nationality;
    }

    public byte[] getUploadIdentityCard() {
        return this.uploadIdentityCard;
    }

    public Employee uploadIdentityCard(byte[] uploadIdentityCard) {
        this.setUploadIdentityCard(uploadIdentityCard);
        return this;
    }

    public void setUploadIdentityCard(byte[] uploadIdentityCard) {
        this.uploadIdentityCard = uploadIdentityCard;
    }

    public String getUploadIdentityCardContentType() {
        return this.uploadIdentityCardContentType;
    }

    public Employee uploadIdentityCardContentType(String uploadIdentityCardContentType) {
        this.uploadIdentityCardContentType = uploadIdentityCardContentType;
        return this;
    }

    public void setUploadIdentityCardContentType(String uploadIdentityCardContentType) {
        this.uploadIdentityCardContentType = uploadIdentityCardContentType;
    }

    public TypeEmployed getTypeEmployed() {
        return this.typeEmployed;
    }

    public Employee typeEmployed(TypeEmployed typeEmployed) {
        this.setTypeEmployed(typeEmployed);
        return this;
    }

    public void setTypeEmployed(TypeEmployed typeEmployed) {
        this.typeEmployed = typeEmployed;
    }

    public String getCityAgency() {
        return this.cityAgency;
    }

    public Employee cityAgency(String cityAgency) {
        this.setCityAgency(cityAgency);
        return this;
    }

    public void setCityAgency(String cityAgency) {
        this.cityAgency = cityAgency;
    }

    public String getResidenceCity() {
        return this.residenceCity;
    }

    public Employee residenceCity(String residenceCity) {
        this.setResidenceCity(residenceCity);
        return this;
    }

    public void setResidenceCity(String residenceCity) {
        this.residenceCity = residenceCity;
    }

    public String getAddress() {
        return this.address;
    }

    public Employee address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSocialSecurityNumber() {
        return this.socialSecurityNumber;
    }

    public Employee socialSecurityNumber(String socialSecurityNumber) {
        this.setSocialSecurityNumber(socialSecurityNumber);
        return this;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public Employee birthDate(Instant birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Employee birthPlace(String birthPlace) {
        this.setBirthPlace(birthPlace);
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public Employee entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Employee releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWorkstation() {
        return this.workstation;
    }

    public Employee workstation(String workstation) {
        this.setWorkstation(workstation);
        return this;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getDescriptionWorkstation() {
        return this.descriptionWorkstation;
    }

    public Employee descriptionWorkstation(String descriptionWorkstation) {
        this.setDescriptionWorkstation(descriptionWorkstation);
        return this;
    }

    public void setDescriptionWorkstation(String descriptionWorkstation) {
        this.descriptionWorkstation = descriptionWorkstation;
    }

    public Department getDepartment() {
        return this.department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Level getLevel() {
        return this.level;
    }

    public Employee level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Long getCoefficient() {
        return this.coefficient;
    }

    public Employee coefficient(Long coefficient) {
        this.setCoefficient(coefficient);
        return this;
    }

    public void setCoefficient(Long coefficient) {
        this.coefficient = coefficient;
    }

    public String getNumberHours() {
        return this.numberHours;
    }

    public Employee numberHours(String numberHours) {
        this.setNumberHours(numberHours);
        return this;
    }

    public void setNumberHours(String numberHours) {
        this.numberHours = numberHours;
    }

    public String getAverageHourlyCost() {
        return this.averageHourlyCost;
    }

    public Employee averageHourlyCost(String averageHourlyCost) {
        this.setAverageHourlyCost(averageHourlyCost);
        return this;
    }

    public void setAverageHourlyCost(String averageHourlyCost) {
        this.averageHourlyCost = averageHourlyCost;
    }

    public Long getMonthlyGrossAmount() {
        return this.monthlyGrossAmount;
    }

    public Employee monthlyGrossAmount(Long monthlyGrossAmount) {
        this.setMonthlyGrossAmount(monthlyGrossAmount);
        return this;
    }

    public void setMonthlyGrossAmount(Long monthlyGrossAmount) {
        this.monthlyGrossAmount = monthlyGrossAmount;
    }

    public Long getCommissionAmount() {
        return this.commissionAmount;
    }

    public Employee commissionAmount(Long commissionAmount) {
        this.setCommissionAmount(commissionAmount);
        return this;
    }

    public void setCommissionAmount(Long commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public ContractType getContractType() {
        return this.contractType;
    }

    public Employee contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public SalaryType getSalaryType() {
        return this.salaryType;
    }

    public Employee salaryType(SalaryType salaryType) {
        this.setSalaryType(salaryType);
        return this;
    }

    public void setSalaryType(SalaryType salaryType) {
        this.salaryType = salaryType;
    }

    public Instant getHireDate() {
        return this.hireDate;
    }

    public Employee hireDate(Instant hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Enterprise getEnterprise() {
        return this.enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Employee enterprise(Enterprise enterprise) {
        this.setEnterprise(enterprise);
        return this;
    }

    public Employee getManagerEmployee() {
        return this.managerEmployee;
    }

    public void setManagerEmployee(Employee employee) {
        this.managerEmployee = employee;
    }

    public Employee managerEmployee(Employee employee) {
        this.setManagerEmployee(employee);
        return this;
    }

    public Set<Employee> getManagers() {
        return this.managers;
    }

    public void setManagers(Set<Employee> employees) {
        if (this.managers != null) {
            this.managers.forEach(i -> i.setManagerEmployee(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setManagerEmployee(this));
        }
        this.managers = employees;
    }

    public Employee managers(Set<Employee> employees) {
        this.setManagers(employees);
        return this;
    }

    public Employee addManager(Employee employee) {
        this.managers.add(employee);
        employee.setManagerEmployee(this);
        return this;
    }

    public Employee removeManager(Employee employee) {
        this.managers.remove(employee);
        employee.setManagerEmployee(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", identityCard='" + getIdentityCard() + "'" +
            ", dateInspiration='" + getDateInspiration() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", uploadIdentityCard='" + getUploadIdentityCard() + "'" +
            ", uploadIdentityCardContentType='" + getUploadIdentityCardContentType() + "'" +
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
            "}";
    }
}
