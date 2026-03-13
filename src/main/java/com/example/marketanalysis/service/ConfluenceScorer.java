package com.example.marketanalysis.service;

import com.example.marketanalysis.model.ConfluenceScore;
import com.example.marketanalysis.model.MarketRegime;
import com.example.marketanalysis.model.SignalType;

/**
 * Layer 6 — Confluence Scoring.
 * Aggregates scores from all analytical layers into the 10-dimension
 * confluence model. A signal is only issued if the total score ≥ 7/10.
 */
public class ConfluenceScorer {

    /**
     * Build a ConfluenceScore from individual dimension assessments.
     *
     * @param htfTrendAlignment       HTF trend alignment score (0–1)
     * @param structuralLevel         key structural level confluence (0–1)
     * @param candlestickConfirmation candlestick pattern confirmation (0–1)
     * @param volumeConfirmation      volume confirmation (0–1)
     * @param momentumAlignment       momentum indicator alignment (0–1)
     * @param regime                  current market regime
     * @param direction               proposed signal direction
     * @param fundamentalSupport      fundamental support score (0–1)
     * @param sentimentPositioning    sentiment positioning score (0–1)
     * @param riskRewardAchievable    whether R:R ≥ 2:1 is achievable (0 or 1)
     * @param majorNewsEventNear      whether a major news event is within window
     * @return the computed ConfluenceScore
     */
    public ConfluenceScore score(double htfTrendAlignment,
                                 double structuralLevel,
                                 double candlestickConfirmation,
                                 double volumeConfirmation,
                                 double momentumAlignment,
                                 MarketRegime regime,
                                 SignalType direction,
                                 double fundamentalSupport,
                                 double sentimentPositioning,
                                 boolean riskRewardAchievable,
                                 boolean majorNewsEventNear) {
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(htfTrendAlignment);
        cs.setStructuralLevelConfluence(structuralLevel);
        cs.setCandlestickConfirmation(candlestickConfirmation);
        cs.setVolumeConfirmation(volumeConfirmation);
        cs.setMomentumAlignment(momentumAlignment);
        cs.setRegimeCompatibility(assessRegimeCompatibility(regime, direction));
        cs.setFundamentalSupport(fundamentalSupport);
        cs.setSentimentPositioning(sentimentPositioning);
        cs.setRiskRewardAchievable(riskRewardAchievable ? 1.0 : 0.0);
        cs.setNoMajorNewsEvent(majorNewsEventNear ? 0.0 : 1.0);
        return cs;
    }

    /**
     * Assess regime compatibility (0.0–1.0).
     * <ul>
     *   <li>Trending regimes favor directional signals.</li>
     *   <li>Range-bound regimes favor mean-reversion signals at boundaries.</li>
     *   <li>Transitional and crisis regimes reduce confidence.</li>
     * </ul>
     */
    double assessRegimeCompatibility(MarketRegime regime, SignalType direction) {
        if (direction == SignalType.STAND_ASIDE) {
            return 0.0;
        }
        switch (regime) {
            case TRENDING:
                return 1.0; // Trending regimes are ideal for directional signals
            case RANGE_BOUND:
                return 0.7; // Range-bound can work for mean-reversion at extremes
            case TRANSITIONAL:
                return 0.4; // Reduce confidence in transitions
            case CRISIS:
                return 0.2; // Fat-tail events = low confidence
            default:
                return 0.5;
        }
    }
}
