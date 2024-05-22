package com.myapp.mongodb.domain;

import java.util.UUID;

public class EnterpriseTestSamples {

    public static Enterprise getEnterpriseSample1() {
        return new Enterprise()
            .id("id1")
            .companyName("companyName1")
            .businessRegisterNumber("businessRegisterNumber1")
            .uniqueIdentificationNumber("uniqueIdentificationNumber1")
            .businessDomicile("businessDomicile1")
            .email("email1")
            .businessPhone("businessPhone1")
            .city("city1");
    }

    public static Enterprise getEnterpriseSample2() {
        return new Enterprise()
            .id("id2")
            .companyName("companyName2")
            .businessRegisterNumber("businessRegisterNumber2")
            .uniqueIdentificationNumber("uniqueIdentificationNumber2")
            .businessDomicile("businessDomicile2")
            .email("email2")
            .businessPhone("businessPhone2")
            .city("city2");
    }

    public static Enterprise getEnterpriseRandomSampleGenerator() {
        return new Enterprise()
            .id(UUID.randomUUID().toString())
            .companyName(UUID.randomUUID().toString())
            .businessRegisterNumber(UUID.randomUUID().toString())
            .uniqueIdentificationNumber(UUID.randomUUID().toString())
            .businessDomicile(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .businessPhone(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString());
    }
}
