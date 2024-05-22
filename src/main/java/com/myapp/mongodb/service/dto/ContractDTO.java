package com.myapp.mongodb.service.dto;

import com.myapp.mongodb.domain.enumeration.ContractType;
import com.myapp.mongodb.domain.enumeration.StatusContract;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.mongodb.domain.Contract} entity.
 */
@Schema(description = "The Contrat.\nAjouter un contrat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractDTO implements Serializable {

    private String id;

    @Schema(description = "fieldName")
    private ContractType contractType;

    private Instant entryDate;

    private Instant releaseDate;

    private StatusContract statusContract;

    private byte[] uploadContract;

    private String uploadContractContentType;

    private EmployeeDTO employee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
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

    public StatusContract getStatusContract() {
        return statusContract;
    }

    public void setStatusContract(StatusContract statusContract) {
        this.statusContract = statusContract;
    }

    public byte[] getUploadContract() {
        return uploadContract;
    }

    public void setUploadContract(byte[] uploadContract) {
        this.uploadContract = uploadContract;
    }

    public String getUploadContractContentType() {
        return uploadContractContentType;
    }

    public void setUploadContractContentType(String uploadContractContentType) {
        this.uploadContractContentType = uploadContractContentType;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractDTO)) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractDTO{" +
            "id='" + getId() + "'" +
            ", contractType='" + getContractType() + "'" +
            ", entryDate='" + getEntryDate() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", statusContract='" + getStatusContract() + "'" +
            ", uploadContract='" + getUploadContract() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
