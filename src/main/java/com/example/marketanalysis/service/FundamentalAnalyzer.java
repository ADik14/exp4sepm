package com.example.marketanalysis.service;

import com.example.marketanalysis.model.AssetClass;
import com.example.marketanalysis.model.SignalType;

/**
 * Layer 4 — Fundamental &amp; Macro Overlay.
 * Evaluates fundamental and macroeconomic factors for each asset class.
 */
public class FundamentalAnalyzer {

    /**
     * Assess fundamental support for the proposed signal direction.
     * Returns a score 0.0–1.0.
     *
     * @param assetClass    the asset class being analyzed
     * @param direction     the proposed signal direction
     * @param dxyTrending   true if DXY is trending higher (bearish for gold)
     * @param realYieldsRising true if real yields are rising (bearish for gold)
     * @param fundingRate   crypto funding rate (extreme positive = longs at risk)
     * @param inventoryBullish true if inventory data is bullish for commodities
     * @return fundamental support score 0.0–1.0
     */
    public double assessFundamentalSupport(AssetClass assetClass,
                                           SignalType direction,
                                           boolean dxyTrending,
                                           boolean realYieldsRising,
                                           double fundingRate,
                                           boolean inventoryBullish) {
        if (direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }

        switch (assetClass) {
            case CRYPTO:
                return assessCryptoFundamentals(direction, fundingRate);
            case PRECIOUS_METALS:
                return assessMetalsFundamentals(direction, dxyTrending, realYieldsRising);
            case COMMODITY_FUTURES:
                return assessCommodityFundamentals(direction, inventoryBullish);
            default:
                return 0.5; // Neutral
        }
    }

    private double assessCryptoFundamentals(SignalType direction, double fundingRate) {
        double score = 0.5; // Start neutral

        // Extreme positive funding (> +0.1%) = longs at risk (counter-signal)
        if (fundingRate > 0.001) {
            if (direction == SignalType.SHORT) {
                score += 0.3; // Supports short
            } else {
                score -= 0.3; // Penalizes long
            }
        }
        // Extreme negative funding (< -0.05%) = shorts at risk
        else if (fundingRate < -0.0005) {
            if (direction == SignalType.LONG) {
                score += 0.3; // Supports long
            } else {
                score -= 0.3; // Penalizes short
            }
        }

        return Math.max(0.0, Math.min(1.0, score));
    }

    private double assessMetalsFundamentals(SignalType direction,
                                            boolean dxyTrending,
                                            boolean realYieldsRising) {
        double score = 0.5;

        // DXY inverse correlation with gold
        if (direction == SignalType.LONG && !dxyTrending) {
            score += 0.25; // Weak DXY supports gold long
        } else if (direction == SignalType.SHORT && dxyTrending) {
            score += 0.25; // Strong DXY supports gold short
        }

        // Real yields inverse correlation with gold
        if (direction == SignalType.LONG && !realYieldsRising) {
            score += 0.25; // Falling real yields support gold long
        } else if (direction == SignalType.SHORT && realYieldsRising) {
            score += 0.25; // Rising real yields support gold short
        }

        return Math.max(0.0, Math.min(1.0, score));
    }

    private double assessCommodityFundamentals(SignalType direction,
                                               boolean inventoryBullish) {
        double score = 0.5;

        if (direction == SignalType.LONG && inventoryBullish) {
            score += 0.3;
        } else if (direction == SignalType.SHORT && !inventoryBullish) {
            score += 0.3;
        }

        return Math.max(0.0, Math.min(1.0, score));
    }

    /**
     * Check whether a high-impact economic event is within the signal horizon.
     *
     * @param minutesToEvent minutes until the next high-impact event
     * @return true if the event is within 30 minutes (flagged as HIGH-RISK)
     */
    public boolean isHighImpactEventNear(int minutesToEvent) {
        return minutesToEvent <= 30;
    }

    /**
     * Check whether position size should be reduced due to proximity to
     * a red-folder economic event (within 4 hours).
     *
     * @param minutesToEvent minutes until the next high-impact event
     * @return true if the position should be reduced by 50%
     */
    public boolean shouldReducePositionSize(int minutesToEvent) {
        return minutesToEvent <= 240; // 4 hours
    }
}
