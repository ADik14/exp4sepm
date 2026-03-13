package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FundamentalAnalyzerTest {

    private FundamentalAnalyzer analyzer;

    @Before
    public void setUp() {
        analyzer = new FundamentalAnalyzer();
    }

    @Test
    public void testCryptoExtremeFundingLongPenalized() {
        // Extreme positive funding > 0.1% penalizes longs
        double score = analyzer.assessFundamentalSupport(
                AssetClass.CRYPTO, SignalType.LONG,
                false, false, 0.002, false);
        assertTrue("Long should be penalized with extreme funding", score < 0.5);
    }

    @Test
    public void testCryptoExtremeFundingShortSupported() {
        // Extreme positive funding > 0.1% supports shorts
        double score = analyzer.assessFundamentalSupport(
                AssetClass.CRYPTO, SignalType.SHORT,
                false, false, 0.002, false);
        assertTrue("Short should be supported with extreme funding", score > 0.5);
    }

    @Test
    public void testGoldLongWithWeakDxy() {
        double score = analyzer.assessFundamentalSupport(
                AssetClass.PRECIOUS_METALS, SignalType.LONG,
                false, false, 0.0, false);
        assertTrue("Gold long with weak DXY should score well", score >= 0.75);
    }

    @Test
    public void testHighImpactEventNear() {
        assertTrue(analyzer.isHighImpactEventNear(15));
        assertTrue(analyzer.isHighImpactEventNear(30));
        assertFalse(analyzer.isHighImpactEventNear(31));
    }

    @Test
    public void testShouldReducePositionSize() {
        assertTrue(analyzer.shouldReducePositionSize(60));
        assertTrue(analyzer.shouldReducePositionSize(240));
        assertFalse(analyzer.shouldReducePositionSize(241));
    }

    @Test
    public void testStandAsideReturnsZero() {
        double score = analyzer.assessFundamentalSupport(
                AssetClass.CRYPTO, SignalType.STAND_ASIDE,
                false, false, 0.0, false);
        assertEquals(0.0, score, 0.001);
    }
}
