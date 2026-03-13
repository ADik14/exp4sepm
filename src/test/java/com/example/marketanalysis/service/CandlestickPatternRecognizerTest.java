package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class CandlestickPatternRecognizerTest {

    private CandlestickPatternRecognizer recognizer;

    @Before
    public void setUp() {
        recognizer = new CandlestickPatternRecognizer();
    }

    @Test
    public void testDetectHammer() {
        // Pin bar / hammer: lower wick >= 2x body, small upper wick
        MarketData candle = new MarketData();
        candle.setOpen(100.0);
        candle.setClose(101.0);  // Body = 1
        candle.setHigh(101.2);   // Upper wick = 0.2
        candle.setLow(97.0);     // Lower wick = 3 (>= 2x body)

        java.util.List<CandlestickPatternRecognizer.PatternResult> patterns =
                recognizer.detectPatterns(
                        Collections.singletonList(candle),
                        97.5,   // support near low
                        105.0,  // resistance far away
                        1.0);

        boolean foundHammer = patterns.stream()
                .anyMatch(p -> p.getPatternName().contains("Hammer"));
        assertTrue("Should detect hammer pattern", foundHammer);
    }

    @Test
    public void testDetectShootingStar() {
        // Shooting star: upper wick >= 2x body, small lower wick
        MarketData candle = new MarketData();
        candle.setOpen(101.0);
        candle.setClose(100.0);  // Body = 1
        candle.setHigh(103.5);   // Upper wick = 2.5 (>= 2x body)
        candle.setLow(99.8);     // Lower wick = 0.2

        java.util.List<CandlestickPatternRecognizer.PatternResult> patterns =
                recognizer.detectPatterns(
                        Collections.singletonList(candle),
                        95.0,   // support far away
                        103.0,  // resistance near high
                        1.0);

        boolean foundStar = patterns.stream()
                .anyMatch(p -> p.getPatternName().contains("Shooting Star"));
        assertTrue("Should detect shooting star", foundStar);
    }

    @Test
    public void testDetectBullishEngulfing() {
        MarketData prev = new MarketData();
        prev.setOpen(102.0);
        prev.setClose(100.0);
        prev.setHigh(102.5);
        prev.setLow(99.5);

        MarketData current = new MarketData();
        current.setOpen(99.5);
        current.setClose(103.0);
        current.setHigh(103.5);
        current.setLow(99.0);

        java.util.List<CandlestickPatternRecognizer.PatternResult> patterns =
                recognizer.detectPatterns(
                        Arrays.asList(prev, current),
                        99.0,
                        110.0,
                        1.0);

        boolean foundEngulfing = patterns.stream()
                .anyMatch(p -> p.getPatternName().contains("Bullish Engulfing"));
        assertTrue("Should detect bullish engulfing", foundEngulfing);
    }

    @Test
    public void testPatternWithoutContextHasZeroWeight() {
        // Hammer far from any structural level
        MarketData candle = new MarketData();
        candle.setOpen(100.0);
        candle.setClose(101.0);
        candle.setHigh(101.2);
        candle.setLow(97.0);

        java.util.List<CandlestickPatternRecognizer.PatternResult> patterns =
                recognizer.detectPatterns(
                        Collections.singletonList(candle),
                        80.0,   // support very far
                        120.0,  // resistance very far
                        1.0);

        for (CandlestickPatternRecognizer.PatternResult p : patterns) {
            if (!p.isAtStructuralLevel()) {
                assertEquals("Pattern without context should have zero weight",
                        0.0, p.getWeight(), 0.001);
            }
        }
    }

    @Test
    public void testEmptyCandlesReturnsNoPatterns() {
        java.util.List<CandlestickPatternRecognizer.PatternResult> patterns =
                recognizer.detectPatterns(
                        Collections.emptyList(), 100.0, 110.0, 1.0);
        assertTrue(patterns.isEmpty());
    }

    @Test
    public void testConfirmationScoreWithMatchingDirection() {
        CandlestickPatternRecognizer.PatternResult pattern =
                new CandlestickPatternRecognizer.PatternResult(
                        "Hammer", "REVERSAL", true, true, SignalType.LONG);

        double score = recognizer.calculateConfirmationScore(
                Collections.singletonList(pattern), SignalType.LONG);
        assertTrue("Score should be > 0 for matching direction", score > 0);
    }

    @Test
    public void testConfirmationScoreWithOppositeDirection() {
        CandlestickPatternRecognizer.PatternResult pattern =
                new CandlestickPatternRecognizer.PatternResult(
                        "Hammer", "REVERSAL", true, true, SignalType.LONG);

        double score = recognizer.calculateConfirmationScore(
                Collections.singletonList(pattern), SignalType.SHORT);
        assertEquals("Score should be 0 for opposite direction", 0.0, score, 0.001);
    }
}
