package com.example.marketanalysis.model;

/**
 * Trade signal direction.
 */
public enum SignalType {
    LONG("▲ LONG"),
    SHORT("▼ SHORT"),
    STAND_ASIDE("◆ STAND ASIDE");

    private final String display;

    SignalType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
