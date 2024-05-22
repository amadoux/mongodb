package com.myapp.mongodb.domain;

import java.util.UUID;

public class ContractTestSamples {

    public static Contract getContractSample1() {
        return new Contract().id("id1");
    }

    public static Contract getContractSample2() {
        return new Contract().id("id2");
    }

    public static Contract getContractRandomSampleGenerator() {
        return new Contract().id(UUID.randomUUID().toString());
    }
}
