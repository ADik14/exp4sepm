package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MarketRegimeClassifierTest {

    private MarketRegimeClassifier classifier;

    @Before
    public void setUp() {
        classifier = new MarketRegimeClassifier();
    }

    @Test
    public void testClassifyTrendingRegime() {
        MarketData data = new MarketData();
        data.setAdx(30.0);
        assertEquals(MarketRegime.TRENDING, classifier.classifyRegime(data, false));
    }

    @Test
    public void testClassifyRangeBound() {
        MarketData data = new MarketData();
        data.setAdx(15.0);
        assertEquals(MarketRegime.RANGE_BOUND, classifier.classifyRegime(data, false));
    }

    @Test
    public void testClassifyCrisis() {
        MarketData data = new MarketData();
        data.setAdx(30.0);
        assertEquals(MarketRegime.CRISIS, classifier.classifyRegime(data, true));
    }

    @Test
    public void testClassifyVolatilityNormal() {
        MarketData data = new MarketData();
        data.setAtr(100.0);
        data.setAverageAtr(100.0);
        assertEquals(VolatilityRegime.NORMAL, classifier.classifyVolatility(data));
    }

    @Test
    public void testClassifyVolatilityExtreme() {
        MarketData data = new MarketData();
        data.setAtr(300.0);
        data.setAverageAtr(100.0);
        assertEquals(VolatilityRegime.EXTREME, classifier.classifyVolatility(data));
    }

    @Test
    public void testClassifyVolatilityZeroAverage() {
        MarketData data = new MarketData();
        data.setAtr(100.0);
        data.setAverageAtr(0.0);
        assertEquals(VolatilityRegime.NORMAL, classifier.classifyVolatility(data));
    }
}
