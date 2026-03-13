package com.example.marketanalysis.service;

import com.example.marketanalysis.model.MarketData;
import com.example.marketanalysis.model.SignalType;

import java.util.ArrayList;
import java.util.List;

/**
 * Layer 3 — Candlestick Pattern Recognition.
 * Identifies reversal and continuation patterns and validates them
 * against structural context. A pattern without context is treated as noise.
 */
public class CandlestickPatternRecognizer {

    /**
     * Recognized candlestick pattern with context validation.
     */
    public static class PatternResult {
        private final String patternName;
        private final String type; // "REVERSAL" or "CONTINUATION"
        private final boolean highConfidence;
        private final boolean atStructuralLevel;
        private final SignalType impliedDirection;

        public PatternResult(String patternName, String type, boolean highConfidence,
                             boolean atStructuralLevel, SignalType impliedDirection) {
            this.patternName = patternName;
            this.type = type;
            this.highConfidence = highConfidence;
            this.atStructuralLevel = atStructuralLevel;
            this.impliedDirection = impliedDirection;
        }

        public String getPatternName() {
            return patternName;
        }

        public String getType() {
            return type;
        }

        public boolean isHighConfidence() {
            return highConfidence;
        }

        public boolean isAtStructuralLevel() {
            return atStructuralLevel;
        }

        public SignalType getImpliedDirection() {
            return impliedDirection;
        }

        /**
         * Score weight: high-confidence patterns at structural levels are weighted 2x.
         */
        public double getWeight() {
            double base = highConfidence ? 2.0 : 1.0;
            return atStructuralLevel ? base : 0.0; // Pattern without context = noise
        }

        @Override
        public String toString() {
            return String.format("%s (%s, %s, %s)",
                    patternName, type,
                    highConfidence ? "HIGH" : "NORMAL",
                    atStructuralLevel ? "at structure" : "no context");
        }
    }

    /**
     * Detect candlestick patterns from recent candle data.
     *
     * @param candles        the list of recent candles (latest last)
     * @param supportLevel   the nearest support level price
     * @param resistanceLevel the nearest resistance level price
     * @param tolerance      price tolerance for level proximity (e.g. ATR-based)
     * @return list of detected patterns
     */
    public List<PatternResult> detectPatterns(List<MarketData> candles,
                                              double supportLevel,
                                              double resistanceLevel,
                                              double tolerance) {
        List<PatternResult> results = new ArrayList<>();
        if (candles == null || candles.isEmpty()) {
            return results;
        }

        MarketData current = candles.get(candles.size() - 1);
        boolean nearSupport = Math.abs(current.getLow() - supportLevel) <= tolerance;
        boolean nearResistance = Math.abs(current.getHigh() - resistanceLevel) <= tolerance;

        // Pin bar / Hammer detection: lower wick >= 2x body
        if (current.getLowerWick() >= 2.0 * current.getBodySize()
                && current.getUpperWick() < current.getBodySize()) {
            results.add(new PatternResult("Hammer/Pin Bar", "REVERSAL",
                    true, nearSupport, SignalType.LONG));
        }

        // Inverted hammer / Shooting star: upper wick >= 2x body
        if (current.getUpperWick() >= 2.0 * current.getBodySize()
                && current.getLowerWick() < current.getBodySize()) {
            results.add(new PatternResult("Shooting Star/Inverted Hammer", "REVERSAL",
                    true, nearResistance, SignalType.SHORT));
        }

        // Doji at extremes
        if (current.isDoji()) {
            if (nearSupport) {
                results.add(new PatternResult("Doji at Support", "REVERSAL",
                        false, true, SignalType.LONG));
            }
            if (nearResistance) {
                results.add(new PatternResult("Doji at Resistance", "REVERSAL",
                        false, true, SignalType.SHORT));
            }
        }

        // Engulfing pattern (requires at least 2 candles)
        if (candles.size() >= 2) {
            MarketData previous = candles.get(candles.size() - 2);
            detectEngulfing(current, previous, nearSupport, nearResistance, results);
        }

        // Inside bar (requires at least 2 candles)
        if (candles.size() >= 2) {
            MarketData previous = candles.get(candles.size() - 2);
            detectInsideBar(current, previous, nearSupport, nearResistance, results);
        }

        return results;
    }

    private void detectEngulfing(MarketData current, MarketData previous,
                                 boolean nearSupport, boolean nearResistance,
                                 List<PatternResult> results) {
        // Bullish engulfing: current bullish, previous bearish, current body fully overlaps
        if (current.isBullish() && previous.isBearish()
                && current.getOpen() <= previous.getClose()
                && current.getClose() >= previous.getOpen()) {
            results.add(new PatternResult("Bullish Engulfing", "REVERSAL",
                    true, nearSupport, SignalType.LONG));
        }

        // Bearish engulfing: current bearish, previous bullish, current body fully overlaps
        if (current.isBearish() && previous.isBullish()
                && current.getOpen() >= previous.getClose()
                && current.getClose() <= previous.getOpen()) {
            results.add(new PatternResult("Bearish Engulfing", "REVERSAL",
                    true, nearResistance, SignalType.SHORT));
        }
    }

    private void detectInsideBar(MarketData current, MarketData previous,
                                 boolean nearSupport, boolean nearResistance,
                                 List<PatternResult> results) {
        // Inside bar: current high < previous high AND current low > previous low
        if (current.getHigh() < previous.getHigh()
                && current.getLow() > previous.getLow()) {
            boolean atLevel = nearSupport || nearResistance;
            SignalType direction = nearSupport ? SignalType.LONG
                    : (nearResistance ? SignalType.SHORT : SignalType.STAND_ASIDE);
            results.add(new PatternResult("Inside Bar", "CONTINUATION",
                    false, atLevel, direction));
        }
    }

    /**
     * Calculate a candlestick confirmation score (0.0–1.0) based on
     * detected patterns and the proposed direction.
     *
     * @param patterns  the detected patterns
     * @param direction the proposed signal direction
     * @return confirmation score
     */
    public double calculateConfirmationScore(List<PatternResult> patterns,
                                             SignalType direction) {
        if (patterns == null || patterns.isEmpty()
                || direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }

        double maxWeight = 0.0;
        for (PatternResult p : patterns) {
            if (p.getImpliedDirection() == direction && p.getWeight() > 0) {
                maxWeight = Math.max(maxWeight, p.getWeight());
            }
        }

        // Normalize: 2.0 (max weight from high-confidence at structure) → 1.0
        return Math.min(1.0, maxWeight / 2.0);
    }
}
