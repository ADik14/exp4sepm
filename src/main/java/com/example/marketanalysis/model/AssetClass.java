package com.example.marketanalysis.model;

/**
 * Supported asset classes for the market analysis agent.
 */
public enum AssetClass {
    CRYPTO("Cryptocurrency"),
    PRECIOUS_METALS("Precious Metals"),
    COMMODITY_FUTURES("Commodity Futures");

    private final String label;

    AssetClass(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Infer the asset class from a ticker symbol.
     *
     * @param symbol the trading symbol (e.g. BTCUSD, XAUUSD, CL1!)
     * @return the asset class
     */
    public static AssetClass fromSymbol(String symbol) {
        String upper = symbol.toUpperCase();
        if (upper.contains("BTC") || upper.contains("ETH") || upper.contains("USDT")
                || upper.contains("SOL") || upper.contains("BNB") || upper.contains("XRP")
                || upper.contains("ADA") || upper.contains("DOGE") || upper.contains("DOT")) {
            return CRYPTO;
        }
        if (upper.contains("XAU") || upper.contains("XAG") || upper.contains("GOLD")
                || upper.contains("SILVER")) {
            return PRECIOUS_METALS;
        }
        return COMMODITY_FUTURES;
    }
}
