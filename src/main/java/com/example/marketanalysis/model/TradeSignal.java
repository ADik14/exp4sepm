package com.example.marketanalysis.model;

/**
 * Complete trade signal output model matching the structured signal format.
 */
public class TradeSignal {

    // Identity
    private String asset;
    private AssetClass assetClass;
    private Timeframe timeframe;
    private SignalType signalType;

    // Confidence
    private ConfluenceScore confluenceScore;
    private int winProbabilityPercent;

    // Levels
    private double entryLow;
    private double entryHigh;
    private double stopLoss;
    private String stopLossBasis;
    private double target1;
    private double target2;
    private double target3;

    // Validity
    private String signalValidity;
    private String invalidationCondition;

    // Trader profiles
    private String scalperNotes;
    private String intradayNotes;
    private String swingNotes;

    // Risk
    private RiskMatrix riskMatrix;

    // Rationale
    private String technicalRationale;
    private String fundamentalContext;

    // MT5 execution
    private String mt5OrderType;
    private String mt5Symbol;
    private String mt5PendingPrice;
    private String mt5Expiry;

    // Market context
    private MarketRegime marketRegime;
    private VolatilityRegime volatilityRegime;

    public TradeSignal() {
    }

    /**
     * Check whether this signal should be issued (confluence ≥ 7/10
     * and R:R ≥ 2:1 for at least the first target).
     */
    public boolean isActionable() {
        if (confluenceScore == null || !confluenceScore.meetsSignalThreshold()) {
            return false;
        }
        if (signalType == SignalType.STAND_ASIDE) {
            return false;
        }
        if (riskMatrix != null && target1 != 0) {
            boolean isLong = signalType == SignalType.LONG;
            if (!riskMatrix.meetsMinRiskReward(target1, isLong)) {
                return false;
            }
        }
        return invalidationCondition != null && !invalidationCondition.isEmpty();
    }

    /**
     * Return the confidence string (e.g. "7.5 / 10").
     */
    public String getConfidenceDisplay() {
        if (confluenceScore == null) {
            return "N/A";
        }
        return String.format("%.1f / 10", confluenceScore.getTotal());
    }

    // --- Getters and Setters ---

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(AssetClass assetClass) {
        this.assetClass = assetClass;
    }

    public Timeframe getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(Timeframe timeframe) {
        this.timeframe = timeframe;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public ConfluenceScore getConfluenceScore() {
        return confluenceScore;
    }

    public void setConfluenceScore(ConfluenceScore confluenceScore) {
        this.confluenceScore = confluenceScore;
    }

    public int getWinProbabilityPercent() {
        return winProbabilityPercent;
    }

    public void setWinProbabilityPercent(int winProbabilityPercent) {
        this.winProbabilityPercent = winProbabilityPercent;
    }

    public double getEntryLow() {
        return entryLow;
    }

    public void setEntryLow(double entryLow) {
        this.entryLow = entryLow;
    }

    public double getEntryHigh() {
        return entryHigh;
    }

    public void setEntryHigh(double entryHigh) {
        this.entryHigh = entryHigh;
    }

    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getStopLossBasis() {
        return stopLossBasis;
    }

    public void setStopLossBasis(String stopLossBasis) {
        this.stopLossBasis = stopLossBasis;
    }

    public double getTarget1() {
        return target1;
    }

    public void setTarget1(double target1) {
        this.target1 = target1;
    }

    public double getTarget2() {
        return target2;
    }

    public void setTarget2(double target2) {
        this.target2 = target2;
    }

    public double getTarget3() {
        return target3;
    }

    public void setTarget3(double target3) {
        this.target3 = target3;
    }

    public String getSignalValidity() {
        return signalValidity;
    }

    public void setSignalValidity(String signalValidity) {
        this.signalValidity = signalValidity;
    }

    public String getInvalidationCondition() {
        return invalidationCondition;
    }

    public void setInvalidationCondition(String invalidationCondition) {
        this.invalidationCondition = invalidationCondition;
    }

    public String getScalperNotes() {
        return scalperNotes;
    }

    public void setScalperNotes(String scalperNotes) {
        this.scalperNotes = scalperNotes;
    }

    public String getIntradayNotes() {
        return intradayNotes;
    }

    public void setIntradayNotes(String intradayNotes) {
        this.intradayNotes = intradayNotes;
    }

    public String getSwingNotes() {
        return swingNotes;
    }

    public void setSwingNotes(String swingNotes) {
        this.swingNotes = swingNotes;
    }

    public RiskMatrix getRiskMatrix() {
        return riskMatrix;
    }

    public void setRiskMatrix(RiskMatrix riskMatrix) {
        this.riskMatrix = riskMatrix;
    }

    public String getTechnicalRationale() {
        return technicalRationale;
    }

    public void setTechnicalRationale(String technicalRationale) {
        this.technicalRationale = technicalRationale;
    }

    public String getFundamentalContext() {
        return fundamentalContext;
    }

    public void setFundamentalContext(String fundamentalContext) {
        this.fundamentalContext = fundamentalContext;
    }

    public String getMt5OrderType() {
        return mt5OrderType;
    }

    public void setMt5OrderType(String mt5OrderType) {
        this.mt5OrderType = mt5OrderType;
    }

    public String getMt5Symbol() {
        return mt5Symbol;
    }

    public void setMt5Symbol(String mt5Symbol) {
        this.mt5Symbol = mt5Symbol;
    }

    public String getMt5PendingPrice() {
        return mt5PendingPrice;
    }

    public void setMt5PendingPrice(String mt5PendingPrice) {
        this.mt5PendingPrice = mt5PendingPrice;
    }

    public String getMt5Expiry() {
        return mt5Expiry;
    }

    public void setMt5Expiry(String mt5Expiry) {
        this.mt5Expiry = mt5Expiry;
    }

    public MarketRegime getMarketRegime() {
        return marketRegime;
    }

    public void setMarketRegime(MarketRegime marketRegime) {
        this.marketRegime = marketRegime;
    }

    public VolatilityRegime getVolatilityRegime() {
        return volatilityRegime;
    }

    public void setVolatilityRegime(VolatilityRegime volatilityRegime) {
        this.volatilityRegime = volatilityRegime;
    }
}
