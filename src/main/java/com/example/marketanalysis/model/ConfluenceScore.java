package com.example.marketanalysis.model;

/**
 * Confluence scoring model with 10 dimensions.
 * Each dimension is scored 0.0 to 1.0, and the total must reach 7/10
 * before a signal can be issued.
 */
public class ConfluenceScore {

    /** Minimum score required to issue a trade signal. */
    public static final double SIGNAL_THRESHOLD = 7.0;

    /** Score at which the setup is labelled "Watch Only". */
    public static final double WATCH_THRESHOLD = 6.0;

    private double htfTrendAlignment;
    private double structuralLevelConfluence;
    private double candlestickConfirmation;
    private double volumeConfirmation;
    private double momentumAlignment;
    private double regimeCompatibility;
    private double fundamentalSupport;
    private double sentimentPositioning;
    private double riskRewardAchievable;
    private double noMajorNewsEvent;

    public ConfluenceScore() {
    }

    public double getTotal() {
        return htfTrendAlignment
                + structuralLevelConfluence
                + candlestickConfirmation
                + volumeConfirmation
                + momentumAlignment
                + regimeCompatibility
                + fundamentalSupport
                + sentimentPositioning
                + riskRewardAchievable
                + noMajorNewsEvent;
    }

    /**
     * Determine whether the confluence score meets the minimum threshold
     * for issuing a trade signal (≥ 7/10).
     */
    public boolean meetsSignalThreshold() {
        return getTotal() >= SIGNAL_THRESHOLD;
    }

    /**
     * Determine whether the confluence score qualifies as "Watch Only"
     * (≥ 6/10 but &lt; 7/10).
     */
    public boolean isWatchOnly() {
        double total = getTotal();
        return total >= WATCH_THRESHOLD && total < SIGNAL_THRESHOLD;
    }

    /**
     * Return the signal verdict text based on the score.
     */
    public String getVerdict() {
        if (meetsSignalThreshold()) {
            double total = getTotal();
            if (total >= 9.0) {
                return "High-conviction setup.";
            }
            return "Moderate-conviction — respect invalidation strictly.";
        }
        if (isWatchOnly()) {
            return "Watch Only — insufficient confluence for signal.";
        }
        return "INSUFFICIENT CONFLUENCE — STAND ASIDE.";
    }

    // --- Getters and Setters ---

    public double getHtfTrendAlignment() {
        return htfTrendAlignment;
    }

    public void setHtfTrendAlignment(double htfTrendAlignment) {
        this.htfTrendAlignment = clamp(htfTrendAlignment);
    }

    public double getStructuralLevelConfluence() {
        return structuralLevelConfluence;
    }

    public void setStructuralLevelConfluence(double structuralLevelConfluence) {
        this.structuralLevelConfluence = clamp(structuralLevelConfluence);
    }

    public double getCandlestickConfirmation() {
        return candlestickConfirmation;
    }

    public void setCandlestickConfirmation(double candlestickConfirmation) {
        this.candlestickConfirmation = clamp(candlestickConfirmation);
    }

    public double getVolumeConfirmation() {
        return volumeConfirmation;
    }

    public void setVolumeConfirmation(double volumeConfirmation) {
        this.volumeConfirmation = clamp(volumeConfirmation);
    }

    public double getMomentumAlignment() {
        return momentumAlignment;
    }

    public void setMomentumAlignment(double momentumAlignment) {
        this.momentumAlignment = clamp(momentumAlignment);
    }

    public double getRegimeCompatibility() {
        return regimeCompatibility;
    }

    public void setRegimeCompatibility(double regimeCompatibility) {
        this.regimeCompatibility = clamp(regimeCompatibility);
    }

    public double getFundamentalSupport() {
        return fundamentalSupport;
    }

    public void setFundamentalSupport(double fundamentalSupport) {
        this.fundamentalSupport = clamp(fundamentalSupport);
    }

    public double getSentimentPositioning() {
        return sentimentPositioning;
    }

    public void setSentimentPositioning(double sentimentPositioning) {
        this.sentimentPositioning = clamp(sentimentPositioning);
    }

    public double getRiskRewardAchievable() {
        return riskRewardAchievable;
    }

    public void setRiskRewardAchievable(double riskRewardAchievable) {
        this.riskRewardAchievable = clamp(riskRewardAchievable);
    }

    public double getNoMajorNewsEvent() {
        return noMajorNewsEvent;
    }

    public void setNoMajorNewsEvent(double noMajorNewsEvent) {
        this.noMajorNewsEvent = clamp(noMajorNewsEvent);
    }

    private static double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    @Override
    public String toString() {
        return String.format(
                "ConfluenceScore{total=%.1f/10, htf=%.1f, struct=%.1f, candle=%.1f, "
                        + "vol=%.1f, mom=%.1f, regime=%.1f, fund=%.1f, sent=%.1f, rr=%.1f, news=%.1f}",
                getTotal(), htfTrendAlignment, structuralLevelConfluence,
                candlestickConfirmation, volumeConfirmation, momentumAlignment,
                regimeCompatibility, fundamentalSupport, sentimentPositioning,
                riskRewardAchievable, noMajorNewsEvent);
    }
}
