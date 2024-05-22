package com.myapp.mongodb.service.dto;

import com.myapp.mongodb.domain.enumeration.SPentType;
import com.myapp.mongodb.domain.enumeration.StatusCharges;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.mongodb.domain.SocialCharges} entity.
 */
@Schema(description = "Ajouter les charges sociales")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialChargesDTO implements Serializable {

    private String id;

    private Instant spentDate;

    private SPentType spentType;

    private StatusCharges statusCharges;

    @NotNull
    private Long amount;

    private String commentText;

    private EmployeeDTO responsableDepense;

    private EnterpriseDTO enterprise;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getSpentDate() {
        return spentDate;
    }

    public void setSpentDate(Instant spentDate) {
        this.spentDate = spentDate;
    }

    public SPentType getSpentType() {
        return spentType;
    }

    public void setSpentType(SPentType spentType) {
        this.spentType = spentType;
    }

    public StatusCharges getStatusCharges() {
        return statusCharges;
    }

    public void setStatusCharges(StatusCharges statusCharges) {
        this.statusCharges = statusCharges;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public EmployeeDTO getResponsableDepense() {
        return responsableDepense;
    }

    public void setResponsableDepense(EmployeeDTO responsableDepense) {
        this.responsableDepense = responsableDepense;
    }

    public EnterpriseDTO getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(EnterpriseDTO enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialChargesDTO)) {
            return false;
        }

        SocialChargesDTO socialChargesDTO = (SocialChargesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, socialChargesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialChargesDTO{" +
            "id='" + getId() + "'" +
            ", spentDate='" + getSpentDate() + "'" +
            ", spentType='" + getSpentType() + "'" +
            ", statusCharges='" + getStatusCharges() + "'" +
            ", amount=" + getAmount() +
            ", commentText='" + getCommentText() + "'" +
            ", responsableDepense=" + getResponsableDepense() +
            ", enterprise=" + getEnterprise() +
            "}";
    }
}
