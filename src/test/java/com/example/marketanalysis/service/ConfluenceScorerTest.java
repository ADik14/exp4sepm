package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfluenceScorerTest {

    private ConfluenceScorer scorer;

    @Before
    public void setUp() {
        scorer = new ConfluenceScorer();
    }

    @Test
    public void testHighConfluenceSignal() {
        ConfluenceScore cs = scorer.score(
                1.0, 1.0, 1.0, 1.0, 1.0,
                MarketRegime.TRENDING, SignalType.LONG,
                1.0, 1.0, true, false);
        assertTrue("Should meet signal threshold", cs.meetsSignalThreshold());
        assertEquals(10.0, cs.getTotal(), 0.01);
    }

    @Test
    public void testLowConfluenceStandAside() {
        ConfluenceScore cs = scorer.score(
                0.3, 0.3, 0.0, 0.3, 0.3,
                MarketRegime.TRANSITIONAL, SignalType.LONG,
                0.3, 0.3, false, true);
        assertFalse("Should not meet threshold", cs.meetsSignalThreshold());
    }

    @Test
    public void testRegimeCompatibilityTrending() {
        double compat = scorer.assessRegimeCompatibility(
                MarketRegime.TRENDING, SignalType.LONG);
        assertEquals(1.0, compat, 0.001);
    }

    @Test
    public void testRegimeCompatibilityCrisis() {
        double compat = scorer.assessRegimeCompatibility(
                MarketRegime.CRISIS, SignalType.SHORT);
        assertEquals(0.2, compat, 0.001);
    }

    @Test
    public void testRegimeCompatibilityStandAside() {
        double compat = scorer.assessRegimeCompatibility(
                MarketRegime.TRENDING, SignalType.STAND_ASIDE);
        assertEquals(0.0, compat, 0.001);
    }

    @Test
    public void testNewsEventReducesScore() {
        ConfluenceScore withNews = scorer.score(
                1.0, 1.0, 1.0, 1.0, 1.0,
                MarketRegime.TRENDING, SignalType.LONG,
                1.0, 1.0, true, true);  // news near

        ConfluenceScore noNews = scorer.score(
                1.0, 1.0, 1.0, 1.0, 1.0,
                MarketRegime.TRENDING, SignalType.LONG,
                1.0, 1.0, true, false); // no news

        assertTrue("News should reduce total score",
                withNews.getTotal() < noNews.getTotal());
    }

    @Test
    public void testRiskRewardNotAchievable() {
        ConfluenceScore cs = scorer.score(
                1.0, 1.0, 1.0, 1.0, 1.0,
                MarketRegime.TRENDING, SignalType.LONG,
                1.0, 1.0, false, false);
        assertEquals(0.0, cs.getRiskRewardAchievable(), 0.001);
    }
}
