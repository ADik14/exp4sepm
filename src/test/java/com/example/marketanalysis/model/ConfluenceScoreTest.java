package com.example.marketanalysis.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConfluenceScoreTest {

    @Test
    public void testTotalCalculation() {
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(1.0);
        cs.setStructuralLevelConfluence(0.8);
        cs.setCandlestickConfirmation(0.7);
        cs.setVolumeConfirmation(0.6);
        cs.setMomentumAlignment(0.9);
        cs.setRegimeCompatibility(1.0);
        cs.setFundamentalSupport(0.5);
        cs.setSentimentPositioning(0.6);
        cs.setRiskRewardAchievable(1.0);
        cs.setNoMajorNewsEvent(1.0);
        assertEquals(8.1, cs.getTotal(), 0.01);
    }

    @Test
    public void testMeetsSignalThreshold() {
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(1.0);
        cs.setStructuralLevelConfluence(1.0);
        cs.setCandlestickConfirmation(1.0);
        cs.setVolumeConfirmation(1.0);
        cs.setMomentumAlignment(1.0);
        cs.setRegimeCompatibility(1.0);
        cs.setFundamentalSupport(1.0);
        cs.setSentimentPositioning(0.0);
        cs.setRiskRewardAchievable(0.0);
        cs.setNoMajorNewsEvent(0.0);
        assertTrue(cs.meetsSignalThreshold()); // 7.0
    }

    @Test
    public void testBelowThreshold() {
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(0.5);
        cs.setStructuralLevelConfluence(0.5);
        cs.setCandlestickConfirmation(0.5);
        cs.setVolumeConfirmation(0.5);
        cs.setMomentumAlignment(0.5);
        cs.setRegimeCompatibility(0.5);
        cs.setFundamentalSupport(0.5);
        cs.setSentimentPositioning(0.5);
        cs.setRiskRewardAchievable(0.5);
        cs.setNoMajorNewsEvent(0.5);
        assertFalse(cs.meetsSignalThreshold()); // 5.0
    }

    @Test
    public void testWatchOnly() {
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(1.0);
        cs.setStructuralLevelConfluence(1.0);
        cs.setCandlestickConfirmation(1.0);
        cs.setVolumeConfirmation(1.0);
        cs.setMomentumAlignment(1.0);
        cs.setRegimeCompatibility(1.0);
        cs.setFundamentalSupport(0.0);
        cs.setSentimentPositioning(0.0);
        cs.setRiskRewardAchievable(0.0);
        cs.setNoMajorNewsEvent(0.0);
        assertTrue(cs.isWatchOnly()); // 6.0
        assertFalse(cs.meetsSignalThreshold());
    }

    @Test
    public void testVerdictHighConviction() {
        ConfluenceScore cs = new ConfluenceScore();
        for (int i = 0; i < 10; i++) {
            cs.setHtfTrendAlignment(1.0);
            cs.setStructuralLevelConfluence(1.0);
            cs.setCandlestickConfirmation(1.0);
            cs.setVolumeConfirmation(1.0);
            cs.setMomentumAlignment(1.0);
            cs.setRegimeCompatibility(1.0);
            cs.setFundamentalSupport(1.0);
            cs.setSentimentPositioning(1.0);
            cs.setRiskRewardAchievable(1.0);
            cs.setNoMajorNewsEvent(1.0);
        }
        assertEquals("High-conviction setup.", cs.getVerdict());
    }

    @Test
    public void testVerdictStandAside() {
        ConfluenceScore cs = new ConfluenceScore();
        assertEquals("INSUFFICIENT CONFLUENCE — STAND ASIDE.", cs.getVerdict());
    }

    @Test
    public void testClampValues() {
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(1.5); // Should be clamped to 1.0
        assertEquals(1.0, cs.getHtfTrendAlignment(), 0.001);
        cs.setHtfTrendAlignment(-0.5); // Should be clamped to 0.0
        assertEquals(0.0, cs.getHtfTrendAlignment(), 0.001);
    }
}
