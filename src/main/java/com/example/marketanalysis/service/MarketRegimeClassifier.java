package com.example.marketanalysis.service;

import com.example.marketanalysis.model.MarketData;
import com.example.marketanalysis.model.MarketRegime;
import com.example.marketanalysis.model.VolatilityRegime;

/**
 * Layer 1 — Market Regime Classification.
 * Classifies the current market into Trending, Range-bound, Transitional,
 * or Crisis regimes, and labels the volatility regime.
 */
public class MarketRegimeClassifier {

    /**
     * Classify the market regime from the supplied market data.
     *
     * @param data    the market data containing ADX
     * @param isCrisis whether an external crisis/fat-tail event is active
     * @return the classified market regime
     */
    public MarketRegime classifyRegime(MarketData data, boolean isCrisis) {
        return MarketRegime.fromAdx(data.getAdx(), isCrisis);
    }

    /**
     * Classify the volatility regime from the supplied market data.
     *
     * @param data the market data containing ATR and average ATR
     * @return the volatility regime
     */
    public VolatilityRegime classifyVolatility(MarketData data) {
        if (data.getAverageAtr() <= 0) {
            return VolatilityRegime.NORMAL;
        }
        double ratio = data.getAtr() / data.getAverageAtr();
        return VolatilityRegime.fromAtrRatio(ratio);
    }
}
