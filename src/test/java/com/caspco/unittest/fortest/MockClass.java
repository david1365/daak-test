package com.caspco.unittest.fortest;

import com.caspco.unittest.model.SampleModel;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */
public class MockClass {
    public static final long actualNumber = 1_000_000L;

    public SampleModel create(Long number) {
        return new SampleModel();
    }

    public static long testStatic() {
        return actualNumber;
    }
}
