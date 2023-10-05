package com.example.servingwebcontent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    @Test
    public void test() {
        int x = 1;
        int y = 14;

        assertEquals(15, x + y);
        assertEquals(14, x * y);
    }
}
