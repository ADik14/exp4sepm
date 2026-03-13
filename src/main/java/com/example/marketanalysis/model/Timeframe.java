package com.example.marketanalysis.model;

/**
 * Chart timeframes for multi-timeframe analysis, ordered from highest to lowest.
 */
public enum Timeframe {
    MONTHLY("Monthly", "MN"),
    WEEKLY("Weekly", "W1"),
    DAILY("Daily", "D1"),
    H4("4-Hour", "H4"),
    H1("1-Hour", "H1"),
    M15("15-Minute", "M15"),
    M5("5-Minute", "M5"),
    M1("1-Minute", "M1");

    private final String label;
    private final String mt5Code;

    Timeframe(String label, String mt5Code) {
        this.label = label;
        this.mt5Code = mt5Code;
    }

    public String getLabel() {
        return label;
    }

    public String getMt5Code() {
        return mt5Code;
    }
}
