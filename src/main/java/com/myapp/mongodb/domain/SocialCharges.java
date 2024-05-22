package com.myapp.mongodb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.mongodb.domain.enumeration.SPentType;
import com.myapp.mongodb.domain.enumeration.StatusCharges;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Ajouter les charges sociales
 */
@Document(collection = "social_charges")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SocialCharges implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("spent_date")
    private Instant spentDate;

    @Field("spent_type")
    private SPentType spentType;

    @Field("status_charges")
    private StatusCharges statusCharges;

    @NotNull
    @Field("amount")
    private Long amount;

    @Field("comment_text")
    private String commentText;

    @DBRef
    @Field("responsableDepense")
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Employee responsableDepense;

    @DBRef
    @Field("enterprise")
    private Enterprise enterprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public SocialCharges id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getSpentDate() {
        return this.spentDate;
    }

    public SocialCharges spentDate(Instant spentDate) {
        this.setSpentDate(spentDate);
        return this;
    }

    public void setSpentDate(Instant spentDate) {
        this.spentDate = spentDate;
    }

    public SPentType getSpentType() {
        return this.spentType;
    }

    public SocialCharges spentType(SPentType spentType) {
        this.setSpentType(spentType);
        return this;
    }

    public void setSpentType(SPentType spentType) {
        this.spentType = spentType;
    }

    public StatusCharges getStatusCharges() {
        return this.statusCharges;
    }

    public SocialCharges statusCharges(StatusCharges statusCharges) {
        this.setStatusCharges(statusCharges);
        return this;
    }

    public void setStatusCharges(StatusCharges statusCharges) {
        this.statusCharges = statusCharges;
    }

    public Long getAmount() {
        return this.amount;
    }

    public SocialCharges amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public SocialCharges commentText(String commentText) {
        this.setCommentText(commentText);
        return this;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Employee getResponsableDepense() {
        return this.responsableDepense;
    }

    public void setResponsableDepense(Employee employee) {
        this.responsableDepense = employee;
    }

    public SocialCharges responsableDepense(Employee employee) {
        this.setResponsableDepense(employee);
        return this;
    }

    public Enterprise getEnterprise() {
        return this.enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public SocialCharges enterprise(Enterprise enterprise) {
        this.setEnterprise(enterprise);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialCharges)) {
            return false;
        }
        return getId() != null && getId().equals(((SocialCharges) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialCharges{" +
            "id=" + getId() +
            ", spentDate='" + getSpentDate() + "'" +
            ", spentType='" + getSpentType() + "'" +
            ", statusCharges='" + getStatusCharges() + "'" +
            ", amount=" + getAmount() +
            ", commentText='" + getCommentText() + "'" +
            "}";
    }
}
