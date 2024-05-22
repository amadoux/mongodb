package com.myapp.mongodb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PaySlip.
 */
@Document(collection = "pay_slip")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaySlip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    /**
     * fieldName
     */
    @Field("net_salary_pay")
    private Long netSalaryPay;

    @Field("pay_slip_date")
    private Instant paySlipDate;

    @Field("upload_pay_slip")
    private byte[] uploadPaySlip;

    @Field("upload_pay_slip_content_type")
    private String uploadPaySlipContentType;

    @DBRef
    @Field("employee")
    @JsonIgnoreProperties(value = { "enterprise", "managerEmployee", "managers" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public PaySlip id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNetSalaryPay() {
        return this.netSalaryPay;
    }

    public PaySlip netSalaryPay(Long netSalaryPay) {
        this.setNetSalaryPay(netSalaryPay);
        return this;
    }

    public void setNetSalaryPay(Long netSalaryPay) {
        this.netSalaryPay = netSalaryPay;
    }

    public Instant getPaySlipDate() {
        return this.paySlipDate;
    }

    public PaySlip paySlipDate(Instant paySlipDate) {
        this.setPaySlipDate(paySlipDate);
        return this;
    }

    public void setPaySlipDate(Instant paySlipDate) {
        this.paySlipDate = paySlipDate;
    }

    public byte[] getUploadPaySlip() {
        return this.uploadPaySlip;
    }

    public PaySlip uploadPaySlip(byte[] uploadPaySlip) {
        this.setUploadPaySlip(uploadPaySlip);
        return this;
    }

    public void setUploadPaySlip(byte[] uploadPaySlip) {
        this.uploadPaySlip = uploadPaySlip;
    }

    public String getUploadPaySlipContentType() {
        return this.uploadPaySlipContentType;
    }

    public PaySlip uploadPaySlipContentType(String uploadPaySlipContentType) {
        this.uploadPaySlipContentType = uploadPaySlipContentType;
        return this;
    }

    public void setUploadPaySlipContentType(String uploadPaySlipContentType) {
        this.uploadPaySlipContentType = uploadPaySlipContentType;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PaySlip employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaySlip)) {
            return false;
        }
        return getId() != null && getId().equals(((PaySlip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaySlip{" +
            "id=" + getId() +
            ", netSalaryPay=" + getNetSalaryPay() +
            ", paySlipDate='" + getPaySlipDate() + "'" +
            ", uploadPaySlip='" + getUploadPaySlip() + "'" +
            ", uploadPaySlipContentType='" + getUploadPaySlipContentType() + "'" +
            "}";
    }
}
