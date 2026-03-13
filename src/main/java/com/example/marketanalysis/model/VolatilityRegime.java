package com.example.marketanalysis.model;

/**
 * Volatility regime classification based on ATR relative to average.
 */
public enum VolatilityRegime {
    LOW("Low", "ATR < 0.5x average"),
    NORMAL("Normal", "ATR ~1x average"),
    ELEVATED("Elevated", "ATR 1.5-2x average"),
    EXTREME("Extreme", "ATR > 2x average");

    private final String label;
    private final String description;

    VolatilityRegime(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Classify volatility regime from ATR ratio (current ATR / average ATR).
     *
     * @param atrRatio ratio of current ATR to its historical average
     * @return the volatility regime
     */
    public static VolatilityRegime fromAtrRatio(double atrRatio) {
        if (atrRatio < 0.5) {
            return LOW;
        }
        if (atrRatio <= 1.5) {
            return NORMAL;
        }
        if (atrRatio <= 2.0) {
            return ELEVATED;
        }
        return EXTREME;
    }
}
