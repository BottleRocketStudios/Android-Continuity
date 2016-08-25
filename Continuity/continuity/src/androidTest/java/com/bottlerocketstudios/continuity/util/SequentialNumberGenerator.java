package com.bottlerocketstudios.continuity.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 8/23/16.
 */
public class SequentialNumberGenerator {

    private static final AtomicInteger NUMBER_GENERATOR = new AtomicInteger();

    public static int generateNumber() {
        return NUMBER_GENERATOR.incrementAndGet();
    }

}
