package com.myapp.mongodb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.mongodb.domain.enumeration.ContractType;
import com.myapp.mongodb.domain.enumeration.StatusContract;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The Contrat.
 * Ajouter un contrat
 */
@Document(collection = "contract")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * fieldName
     */
    @Field("contract_type")
    private ContractType contractType;

    @Field("entry_date")
    private Instant entryDate;

    @Field("release_date")
    private Instant releaseDate;

    @Field("status_contract")
    private StatusContract statusContract;

    @Field("upload_contract")
    private byte[] uploadContract;

    @NotNull
    @Field("upload_contract_content_type")
    private String uploadContractContentType;

    @DBRef
    @Field("employee")
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Contract id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContractType getContractType() {
        return this.contractType;
    }

    public Contract contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Instant getEntryDate() {
        return this.entryDate;
    }

    public Contract entryDate(Instant entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(Instant entryDate) {
        this.entryDate = entryDate;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Contract releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public StatusContract getStatusContract() {
        return this.statusContract;
    }

    public Contract statusContract(StatusContract statusContract) {
        this.setStatusContract(statusContract);
        return this;
    }

    public void setStatusContract(StatusContract statusContract) {
        this.statusContract = statusContract;
    }

    public byte[] getUploadContract() {
        return this.uploadContract;
    }

    public Contract uploadContract(byte[] uploadContract) {
        this.setUploadContract(uploadContract);
        return this;
    }

    public void setUploadContract(byte[] uploadContract) {
        this.uploadContract = uploadContract;
    }

    public String getUploadContractContentType() {
        return this.uploadContractContentType;
    }

    public Contract uploadContractContentType(String uploadContractContentType) {
        this.uploadContractContentType = uploadContractContentType;
        return this;
    }

    public void setUploadContractContentType(String uploadContractContentType) {
        this.uploadContractContentType = uploadContractContentType;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Contract employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", contractType='" + getContractType() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", statusContract='" + getStatusContract() + "'" +
            ", uploadContract='" + getUploadContract() + "'" +
            ", uploadContractContentType='" + getUploadContractContentType() + "'" +
            "}";
    }
}
