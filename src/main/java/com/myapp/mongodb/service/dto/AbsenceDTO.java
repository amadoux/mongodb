package com.myapp.mongodb.service.dto;

import com.myapp.mongodb.domain.enumeration.ConfirmationAbsence;
import com.myapp.mongodb.domain.enumeration.TypeAbsence;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.mongodb.domain.Absence} entity.
 */
@Schema(description = "ajouter une absence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AbsenceDTO implements Serializable {

    private String id;

    private Instant startDate;

    private Instant endDate;

    private Long numberDayAbsence;

    private TypeAbsence typeAbsence;

    private ConfirmationAbsence confirmationAbsence;

    private Long congeRestant;

    private EmployeeDTO employee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getNumberDayAbsence() {
        return numberDayAbsence;
    }

    public void setNumberDayAbsence(Long numberDayAbsence) {
        this.numberDayAbsence = numberDayAbsence;
    }

    public TypeAbsence getTypeAbsence() {
        return typeAbsence;
    }

    public void setTypeAbsence(TypeAbsence typeAbsence) {
        this.typeAbsence = typeAbsence;
    }

    public ConfirmationAbsence getConfirmationAbsence() {
        return confirmationAbsence;
    }

    public void setConfirmationAbsence(ConfirmationAbsence confirmationAbsence) {
        this.confirmationAbsence = confirmationAbsence;
    }

    public Long getCongeRestant() {
        return congeRestant;
    }

    public void setCongeRestant(Long congeRestant) {
        this.congeRestant = congeRestant;
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
        if (!(o instanceof AbsenceDTO)) {
            return false;
        }

        AbsenceDTO absenceDTO = (AbsenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, absenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AbsenceDTO{" +
            "id='" + getId() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", numberDayAbsence=" + getNumberDayAbsence() +
            ", typeAbsence='" + getTypeAbsence() + "'" +
            ", confirmationAbsence='" + getConfirmationAbsence() + "'" +
            ", congeRestant=" + getCongeRestant() +
            ", employee=" + getEmployee() +
            "}";
    }
}
