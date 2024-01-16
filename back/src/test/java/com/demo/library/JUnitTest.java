package com.demo.library;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JUnitTest {
    @DisplayName("JUnit Working Test")
    @Test
    public void assertionTest() {
        String expected = "Hello, JUnit";
        String actual = "Hello, JUnit";

        assertEquals(expected, actual);
    }
}