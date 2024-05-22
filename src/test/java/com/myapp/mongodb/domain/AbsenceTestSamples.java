package com.myapp.mongodb.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AbsenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Absence getAbsenceSample1() {
        return new Absence().id("id1").numberDayAbsence(1L).congeRestant(1L);
    }

    public static Absence getAbsenceSample2() {
        return new Absence().id("id2").numberDayAbsence(2L).congeRestant(2L);
    }

    public static Absence getAbsenceRandomSampleGenerator() {
        return new Absence()
            .id(UUID.randomUUID().toString())
            .numberDayAbsence(longCount.incrementAndGet())
            .congeRestant(longCount.incrementAndGet());
    }
}
