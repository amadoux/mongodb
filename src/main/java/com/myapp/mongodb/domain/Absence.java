package com.myapp.mongodb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapp.mongodb.domain.enumeration.ConfirmationAbsence;
import com.myapp.mongodb.domain.enumeration.TypeAbsence;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * ajouter une absence
 */
@Document(collection = "absence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Absence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @Field("number_day_absence")
    private Long numberDayAbsence;

    @Field("type_absence")
    private TypeAbsence typeAbsence;

    @Field("confirmation_absence")
    private ConfirmationAbsence confirmationAbsence;

    @Field("conge_restant")
    private Long congeRestant;

    @DBRef
    @Field("employee")
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Absence id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Absence startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Absence endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getNumberDayAbsence() {
        return this.numberDayAbsence;
    }

    public Absence numberDayAbsence(Long numberDayAbsence) {
        this.setNumberDayAbsence(numberDayAbsence);
        return this;
    }

    public void setNumberDayAbsence(Long numberDayAbsence) {
        this.numberDayAbsence = numberDayAbsence;
    }

    public TypeAbsence getTypeAbsence() {
        return this.typeAbsence;
    }

    public Absence typeAbsence(TypeAbsence typeAbsence) {
        this.setTypeAbsence(typeAbsence);
        return this;
    }

    public void setTypeAbsence(TypeAbsence typeAbsence) {
        this.typeAbsence = typeAbsence;
    }

    public ConfirmationAbsence getConfirmationAbsence() {
        return this.confirmationAbsence;
    }

    public Absence confirmationAbsence(ConfirmationAbsence confirmationAbsence) {
        this.setConfirmationAbsence(confirmationAbsence);
        return this;
    }

    public void setConfirmationAbsence(ConfirmationAbsence confirmationAbsence) {
        this.confirmationAbsence = confirmationAbsence;
    }

    public Long getCongeRestant() {
        return this.congeRestant;
    }

    public Absence congeRestant(Long congeRestant) {
        this.setCongeRestant(congeRestant);
        return this;
    }

    public void setCongeRestant(Long congeRestant) {
        this.congeRestant = congeRestant;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Absence employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Absence)) {
            return false;
        }
        return getId() != null && getId().equals(((Absence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Absence{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", numberDayAbsence=" + getNumberDayAbsence() +
            ", typeAbsence='" + getTypeAbsence() + "'" +
            ", confirmationAbsence='" + getConfirmationAbsence() + "'" +
            ", congeRestant=" + getCongeRestant() +
            "}";
    }
}
