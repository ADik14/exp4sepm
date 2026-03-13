package com.example.marketanalysis.service;

import com.example.marketanalysis.model.SignalType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SentimentAnalyzerTest {

    private SentimentAnalyzer analyzer;

    @Before
    public void setUp() {
        analyzer = new SentimentAnalyzer();
    }

    @Test
    public void testExtremeFearBullish() {
        // Extreme fear = contrarian bullish
        double score = analyzer.assessFearGreed(SignalType.LONG, 10);
        assertEquals(1.0, score, 0.001);
    }

    @Test
    public void testExtremeGreedBearish() {
        // Extreme greed = contrarian bearish
        double score = analyzer.assessFearGreed(SignalType.SHORT, 90);
        assertEquals(1.0, score, 0.001);
    }

    @Test
    public void testExtremeGreedNotBullish() {
        double score = analyzer.assessFearGreed(SignalType.LONG, 90);
        assertEquals(0.0, score, 0.001);
    }

    @Test
    public void testOverallSentimentScore() {
        double score = analyzer.assessSentiment(
                SignalType.LONG, 15, 1.3, -5000);
        // Extreme fear (1.0) + high P/C (0.8) + negative COT (0.7) / 3
        assertTrue("Sentiment should be supportive for long", score > 0.5);
    }

    @Test
    public void testStandAsideReturnsZero() {
        double score = analyzer.assessSentiment(
                SignalType.STAND_ASIDE, 50, 1.0, 0);
        assertEquals(0.0, score, 0.001);
    }
}
