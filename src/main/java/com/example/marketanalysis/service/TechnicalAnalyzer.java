package com.example.marketanalysis.service;

import com.example.marketanalysis.model.MarketData;
import com.example.marketanalysis.model.SignalType;

import java.util.List;

/**
 * Layer 2 — Multi-Timeframe Technical Analysis.
 * Analyzes structure, momentum, moving average alignment, and volume
 * across the timeframe cascade: Monthly → Weekly → Daily → 4H → 1H → 15M → 5M.
 */
public class TechnicalAnalyzer {

    /**
     * Assess the higher-timeframe trend alignment.
     * Returns a score 0.0–1.0 indicating how aligned the HTF trend is with
     * the proposed signal direction.
     *
     * @param htfData list of higher-timeframe data (e.g. Daily, Weekly)
     * @param direction the proposed signal direction
     * @return alignment score 0.0–1.0
     */
    public double assessHtfTrendAlignment(List<MarketData> htfData, SignalType direction) {
        if (htfData == null || htfData.isEmpty() || direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }

        double score = 0.0;
        int count = 0;

        for (MarketData data : htfData) {
            boolean emaBullish = data.getEma9() > data.getEma21()
                    && data.getEma21() > data.getEma50();
            boolean emaBearish = data.getEma9() < data.getEma21()
                    && data.getEma21() < data.getEma50();

            if (direction == SignalType.LONG && emaBullish) {
                score += 1.0;
            } else if (direction == SignalType.SHORT && emaBearish) {
                score += 1.0;
            } else if (direction == SignalType.LONG && data.getClose() > data.getEma200()) {
                score += 0.5;
            } else if (direction == SignalType.SHORT && data.getClose() < data.getEma200()) {
                score += 0.5;
            }
            count++;
        }

        return count > 0 ? Math.min(1.0, score / count) : 0.0;
    }

    /**
     * Assess momentum indicator alignment (RSI, MACD, Stochastic).
     * Returns a score 0.0–1.0.
     *
     * @param data the market data with indicator values
     * @param direction the proposed signal direction
     * @return momentum alignment score
     */
    public double assessMomentumAlignment(MarketData data, SignalType direction) {
        if (data == null || direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }

        double score = 0.0;

        // RSI assessment
        if (direction == SignalType.LONG) {
            if (data.getRsi() > 50 && data.getRsi() < 70) {
                score += 0.33;
            } else if (data.getRsi() <= 30) {
                // Oversold — strong long signal
                score += 0.33;
            }
        } else {
            if (data.getRsi() < 50 && data.getRsi() > 30) {
                score += 0.33;
            } else if (data.getRsi() >= 70) {
                // Overbought — strong short signal
                score += 0.33;
            }
        }

        // MACD histogram assessment
        if (direction == SignalType.LONG && data.getMacdHistogram() > 0) {
            score += 0.33;
        } else if (direction == SignalType.SHORT && data.getMacdHistogram() < 0) {
            score += 0.33;
        }

        // Stochastic assessment
        if (direction == SignalType.LONG && data.getStochK() > data.getStochD()) {
            score += 0.34;
        } else if (direction == SignalType.SHORT && data.getStochK() < data.getStochD()) {
            score += 0.34;
        }

        return Math.min(1.0, score);
    }

    /**
     * Assess volume confirmation.
     * Returns a score 0.0–1.0 based on VWAP relationship and volume level.
     *
     * @param data the market data
     * @param direction the proposed signal direction
     * @param averageVolume the average volume for comparison
     * @return volume confirmation score
     */
    public double assessVolumeConfirmation(MarketData data, SignalType direction,
                                           double averageVolume) {
        if (data == null || direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }

        double score = 0.0;

        // Above-average volume
        if (averageVolume > 0 && data.getVolume() > averageVolume) {
            score += 0.5;
        }

        // VWAP relationship
        if (direction == SignalType.LONG && data.getClose() > data.getVwap()) {
            score += 0.5;
        } else if (direction == SignalType.SHORT && data.getClose() < data.getVwap()) {
            score += 0.5;
        }

        return Math.min(1.0, score);
    }

    /**
     * Detect whether the EMA stack shows a Golden Cross (bullish) or
     * Death Cross (bearish) proximity.
     *
     * @param data the market data with EMA values
     * @return "GOLDEN_CROSS" if EMA50 recently crossed above EMA200,
     *         "DEATH_CROSS" if EMA50 recently crossed below EMA200,
     *         "NONE" otherwise
     */
    public String detectMaCross(MarketData data) {
        if (data == null) {
            return "NONE";
        }
        double diff = data.getEma50() - data.getEma200();
        double percentDiff = data.getEma200() != 0
                ? Math.abs(diff / data.getEma200()) * 100 : 0;

        if (diff > 0 && percentDiff < 1.0) {
            return "GOLDEN_CROSS";
        }
        if (diff < 0 && percentDiff < 1.0) {
            return "DEATH_CROSS";
        }
        return "NONE";
    }
}
