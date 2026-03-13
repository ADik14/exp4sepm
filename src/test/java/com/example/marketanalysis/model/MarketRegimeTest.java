package com.example.marketanalysis.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MarketRegimeTest {

    @Test
    public void testTrendingRegimeAbove25() {
        assertEquals(MarketRegime.TRENDING, MarketRegime.fromAdx(30.0, false));
        assertEquals(MarketRegime.TRENDING, MarketRegime.fromAdx(25.1, false));
    }

    @Test
    public void testRangeBoundRegimeBelow20() {
        assertEquals(MarketRegime.RANGE_BOUND, MarketRegime.fromAdx(15.0, false));
        assertEquals(MarketRegime.RANGE_BOUND, MarketRegime.fromAdx(19.9, false));
    }

    @Test
    public void testTransitionalRegimeBetween20And25() {
        assertEquals(MarketRegime.TRANSITIONAL, MarketRegime.fromAdx(20.0, false));
        assertEquals(MarketRegime.TRANSITIONAL, MarketRegime.fromAdx(22.5, false));
        assertEquals(MarketRegime.TRANSITIONAL, MarketRegime.fromAdx(25.0, false));
    }

    @Test
    public void testCrisisOverridesAdx() {
        assertEquals(MarketRegime.CRISIS, MarketRegime.fromAdx(30.0, true));
        assertEquals(MarketRegime.CRISIS, MarketRegime.fromAdx(10.0, true));
    }

    @Test
    public void testLabelsNotNull() {
        for (MarketRegime r : MarketRegime.values()) {
            assertNotNull(r.getLabel());
            assertNotNull(r.getDescription());
        }
    }
}
