package com.example.marketanalysis.model;

/**
 * Market regime classification based on ADX values.
 * <ul>
 *   <li>TRENDING: ADX &gt; 25</li>
 *   <li>RANGE_BOUND: ADX &lt; 20</li>
 *   <li>TRANSITIONAL: ADX between 20 and 25</li>
 *   <li>CRISIS: Fat-tail / extreme dislocation event</li>
 * </ul>
 */
public enum MarketRegime {
    TRENDING("Trending", "ADX > 25"),
    RANGE_BOUND("Range-bound", "ADX < 20"),
    TRANSITIONAL("Transitional", "ADX 20-25"),
    CRISIS("Crisis/Fat-tail", "Extreme dislocation");

    private final String label;
    private final String description;

    MarketRegime(String label, String description) {
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
     * Classify regime from ADX value.
     *
     * @param adx the ADX indicator value
     * @param isCrisis whether a crisis/fat-tail event is detected
     * @return the market regime
     */
    public static MarketRegime fromAdx(double adx, boolean isCrisis) {
        if (isCrisis) {
            return CRISIS;
        }
        if (adx > 25) {
            return TRENDING;
        }
        if (adx < 20) {
            return RANGE_BOUND;
        }
        return TRANSITIONAL;
    }
}
