package com.example.marketanalysis.service;

import com.example.marketanalysis.model.SignalType;

/**
 * Layer 5 — Sentiment &amp; Order Flow Intelligence.
 * Evaluates retail sentiment, options market data, institutional positioning,
 * and social sentiment for contrarian and confirmation signals.
 */
public class SentimentAnalyzer {

    /** Fear &amp; Greed index thresholds. */
    private static final int EXTREME_FEAR = 20;
    private static final int FEAR = 40;
    private static final int GREED = 60;
    private static final int EXTREME_GREED = 80;

    /** Put/Call ratio thresholds. */
    private static final double PUT_CALL_BEARISH = 1.2;
    private static final double PUT_CALL_BULLISH = 0.7;

    /**
     * Assess sentiment positioning support for the proposed direction.
     * Returns a score 0.0–1.0.
     *
     * @param direction        the proposed signal direction
     * @param fearGreedIndex   Fear &amp; Greed index (0–100)
     * @param putCallRatio     Put/Call ratio
     * @param cotNetPositioning COT non-commercial net positioning (positive = net long)
     * @return sentiment support score
     */
    public double assessSentiment(SignalType direction,
                                  int fearGreedIndex,
                                  double putCallRatio,
                                  double cotNetPositioning) {
        if (direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }

        double score = 0.0;
        int factors = 0;

        // Fear & Greed as contrarian signal
        score += assessFearGreed(direction, fearGreedIndex);
        factors++;

        // Put/Call ratio
        score += assessPutCallRatio(direction, putCallRatio);
        factors++;

        // COT positioning
        score += assessCotPositioning(direction, cotNetPositioning);
        factors++;

        return factors > 0 ? Math.min(1.0, score / factors) : 0.0;
    }

    /**
     * Assess Fear &amp; Greed as a contrarian signal.
     * Extreme fear = bullish contrarian; extreme greed = bearish contrarian.
     */
    double assessFearGreed(SignalType direction, int fearGreedIndex) {
        if (direction == SignalType.LONG) {
            if (fearGreedIndex <= EXTREME_FEAR) {
                return 1.0; // Extreme fear = contrarian bullish
            }
            if (fearGreedIndex <= FEAR) {
                return 0.7;
            }
            if (fearGreedIndex >= EXTREME_GREED) {
                return 0.0; // Extreme greed = crowded long, risky for longs
            }
            return 0.5;
        } else {
            if (fearGreedIndex >= EXTREME_GREED) {
                return 1.0; // Extreme greed = contrarian bearish
            }
            if (fearGreedIndex >= GREED) {
                return 0.7;
            }
            if (fearGreedIndex <= EXTREME_FEAR) {
                return 0.0; // Extreme fear = crowded short, risky for shorts
            }
            return 0.5;
        }
    }

    double assessPutCallRatio(SignalType direction, double putCallRatio) {
        if (direction == SignalType.LONG && putCallRatio >= PUT_CALL_BEARISH) {
            return 0.8; // High put/call = contrarian bullish
        }
        if (direction == SignalType.SHORT && putCallRatio <= PUT_CALL_BULLISH) {
            return 0.8; // Low put/call = contrarian bearish
        }
        return 0.5;
    }

    double assessCotPositioning(SignalType direction, double cotNetPositioning) {
        if (direction == SignalType.LONG && cotNetPositioning < 0) {
            return 0.7; // Institutions net short = contrarian bullish
        }
        if (direction == SignalType.SHORT && cotNetPositioning > 0) {
            return 0.7; // Institutions net long = contrarian bearish
        }
        return 0.4;
    }
}
