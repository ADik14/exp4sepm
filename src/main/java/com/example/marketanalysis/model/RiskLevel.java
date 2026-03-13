package com.example.marketanalysis.model;

/**
 * Risk level classification for a trade signal.
 */
public enum RiskLevel {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    EXTREME("EXTREME");

    private final String display;

    RiskLevel(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
