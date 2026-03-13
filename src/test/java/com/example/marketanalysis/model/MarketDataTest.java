package com.example.marketanalysis.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MarketDataTest {

    @Test
    public void testBullishCandle() {
        MarketData md = new MarketData();
        md.setOpen(100.0);
        md.setClose(110.0);
        md.setHigh(115.0);
        md.setLow(98.0);
        assertTrue(md.isBullish());
        assertFalse(md.isBearish());
    }

    @Test
    public void testBearishCandle() {
        MarketData md = new MarketData();
        md.setOpen(110.0);
        md.setClose(100.0);
        md.setHigh(115.0);
        md.setLow(98.0);
        assertFalse(md.isBullish());
        assertTrue(md.isBearish());
    }

    @Test
    public void testBodySize() {
        MarketData md = new MarketData();
        md.setOpen(100.0);
        md.setClose(110.0);
        assertEquals(10.0, md.getBodySize(), 0.001);
    }

    @Test
    public void testUpperWick() {
        MarketData md = new MarketData();
        md.setOpen(100.0);
        md.setClose(110.0);
        md.setHigh(115.0);
        md.setLow(98.0);
        assertEquals(5.0, md.getUpperWick(), 0.001);
    }

    @Test
    public void testLowerWick() {
        MarketData md = new MarketData();
        md.setOpen(100.0);
        md.setClose(110.0);
        md.setHigh(115.0);
        md.setLow(98.0);
        assertEquals(2.0, md.getLowerWick(), 0.001);
    }

    @Test
    public void testDoji() {
        MarketData md = new MarketData();
        md.setOpen(100.0);
        md.setClose(100.5);
        md.setHigh(105.0);
        md.setLow(95.0);
        // Body = 0.5, Range = 10, ratio = 0.05 < 0.1 → doji
        assertTrue(md.isDoji());
    }

    @Test
    public void testNotDoji() {
        MarketData md = new MarketData();
        md.setOpen(100.0);
        md.setClose(108.0);
        md.setHigh(110.0);
        md.setLow(99.0);
        // Body = 8, Range = 11, ratio = 0.727 → not doji
        assertFalse(md.isDoji());
    }
}
