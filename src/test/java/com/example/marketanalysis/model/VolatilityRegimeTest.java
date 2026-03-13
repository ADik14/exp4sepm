package com.example.marketanalysis.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class VolatilityRegimeTest {

    @Test
    public void testLowVolatility() {
        assertEquals(VolatilityRegime.LOW, VolatilityRegime.fromAtrRatio(0.3));
        assertEquals(VolatilityRegime.LOW, VolatilityRegime.fromAtrRatio(0.49));
    }

    @Test
    public void testNormalVolatility() {
        assertEquals(VolatilityRegime.NORMAL, VolatilityRegime.fromAtrRatio(0.5));
        assertEquals(VolatilityRegime.NORMAL, VolatilityRegime.fromAtrRatio(1.0));
        assertEquals(VolatilityRegime.NORMAL, VolatilityRegime.fromAtrRatio(1.5));
    }

    @Test
    public void testElevatedVolatility() {
        assertEquals(VolatilityRegime.ELEVATED, VolatilityRegime.fromAtrRatio(1.51));
        assertEquals(VolatilityRegime.ELEVATED, VolatilityRegime.fromAtrRatio(2.0));
    }

    @Test
    public void testExtremeVolatility() {
        assertEquals(VolatilityRegime.EXTREME, VolatilityRegime.fromAtrRatio(2.1));
        assertEquals(VolatilityRegime.EXTREME, VolatilityRegime.fromAtrRatio(5.0));
    }
}
