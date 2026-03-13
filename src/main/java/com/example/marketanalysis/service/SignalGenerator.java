package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;

import java.util.List;

/**
 * Orchestrates all analytical layers to generate a complete trade signal.
 * Executes the layers in sequence per the analytical framework specification.
 */
public class SignalGenerator {

    private final MarketRegimeClassifier regimeClassifier;
    private final TechnicalAnalyzer technicalAnalyzer;
    private final CandlestickPatternRecognizer patternRecognizer;
    private final FundamentalAnalyzer fundamentalAnalyzer;
    private final SentimentAnalyzer sentimentAnalyzer;
    private final ConfluenceScorer confluenceScorer;
    private final SignalFormatter formatter;

    public SignalGenerator() {
        this.regimeClassifier = new MarketRegimeClassifier();
        this.technicalAnalyzer = new TechnicalAnalyzer();
        this.patternRecognizer = new CandlestickPatternRecognizer();
        this.fundamentalAnalyzer = new FundamentalAnalyzer();
        this.sentimentAnalyzer = new SentimentAnalyzer();
        this.confluenceScorer = new ConfluenceScorer();
        this.formatter = new SignalFormatter();
    }

    /**
     * Input parameters for signal generation, encapsulating all required data.
     */
    public static class SignalInput {
        // Market data
        private List<MarketData> htfData;
        private List<MarketData> recentCandles;
        private MarketData currentData;

        // Structural levels
        private double supportLevel;
        private double resistanceLevel;
        private double atrTolerance;
        private double averageVolume;

        // Proposed direction
        private SignalType proposedDirection;

        // Fundamental inputs
        private boolean dxyTrending;
        private boolean realYieldsRising;
        private boolean inventoryBullish;
        private boolean isCrisis;
        private int minutesToNextEvent;

        // Sentiment inputs
        private int fearGreedIndex;
        private double putCallRatio;
        private double cotNetPositioning;

        // Risk inputs
        private double accountBalance;
        private double accountRiskPercent;

        // Trade levels
        private double entryLow;
        private double entryHigh;
        private double stopLoss;
        private String stopLossBasis;
        private double target1;
        private double target2;
        private double target3;

        // Metadata
        private String asset;
        private Timeframe timeframe;
        private String invalidationCondition;
        private String signalValidity;
        private String technicalRationale;
        private String fundamentalContext;
        private String mt5Symbol;
        private String mt5OrderType;

        // --- Getters and Setters ---

        public List<MarketData> getHtfData() { return htfData; }
        public void setHtfData(List<MarketData> htfData) { this.htfData = htfData; }
        public List<MarketData> getRecentCandles() { return recentCandles; }
        public void setRecentCandles(List<MarketData> recentCandles) { this.recentCandles = recentCandles; }
        public MarketData getCurrentData() { return currentData; }
        public void setCurrentData(MarketData currentData) { this.currentData = currentData; }
        public double getSupportLevel() { return supportLevel; }
        public void setSupportLevel(double supportLevel) { this.supportLevel = supportLevel; }
        public double getResistanceLevel() { return resistanceLevel; }
        public void setResistanceLevel(double resistanceLevel) { this.resistanceLevel = resistanceLevel; }
        public double getAtrTolerance() { return atrTolerance; }
        public void setAtrTolerance(double atrTolerance) { this.atrTolerance = atrTolerance; }
        public double getAverageVolume() { return averageVolume; }
        public void setAverageVolume(double averageVolume) { this.averageVolume = averageVolume; }
        public SignalType getProposedDirection() { return proposedDirection; }
        public void setProposedDirection(SignalType proposedDirection) { this.proposedDirection = proposedDirection; }
        public boolean isDxyTrending() { return dxyTrending; }
        public void setDxyTrending(boolean dxyTrending) { this.dxyTrending = dxyTrending; }
        public boolean isRealYieldsRising() { return realYieldsRising; }
        public void setRealYieldsRising(boolean realYieldsRising) { this.realYieldsRising = realYieldsRising; }
        public boolean isInventoryBullish() { return inventoryBullish; }
        public void setInventoryBullish(boolean inventoryBullish) { this.inventoryBullish = inventoryBullish; }
        public boolean isCrisis() { return isCrisis; }
        public void setCrisis(boolean crisis) { isCrisis = crisis; }
        public int getMinutesToNextEvent() { return minutesToNextEvent; }
        public void setMinutesToNextEvent(int minutesToNextEvent) { this.minutesToNextEvent = minutesToNextEvent; }
        public int getFearGreedIndex() { return fearGreedIndex; }
        public void setFearGreedIndex(int fearGreedIndex) { this.fearGreedIndex = fearGreedIndex; }
        public double getPutCallRatio() { return putCallRatio; }
        public void setPutCallRatio(double putCallRatio) { this.putCallRatio = putCallRatio; }
        public double getCotNetPositioning() { return cotNetPositioning; }
        public void setCotNetPositioning(double cotNetPositioning) { this.cotNetPositioning = cotNetPositioning; }
        public double getAccountBalance() { return accountBalance; }
        public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }
        public double getAccountRiskPercent() { return accountRiskPercent; }
        public void setAccountRiskPercent(double accountRiskPercent) { this.accountRiskPercent = accountRiskPercent; }
        public double getEntryLow() { return entryLow; }
        public void setEntryLow(double entryLow) { this.entryLow = entryLow; }
        public double getEntryHigh() { return entryHigh; }
        public void setEntryHigh(double entryHigh) { this.entryHigh = entryHigh; }
        public double getStopLoss() { return stopLoss; }
        public void setStopLoss(double stopLoss) { this.stopLoss = stopLoss; }
        public String getStopLossBasis() { return stopLossBasis; }
        public void setStopLossBasis(String stopLossBasis) { this.stopLossBasis = stopLossBasis; }
        public double getTarget1() { return target1; }
        public void setTarget1(double target1) { this.target1 = target1; }
        public double getTarget2() { return target2; }
        public void setTarget2(double target2) { this.target2 = target2; }
        public double getTarget3() { return target3; }
        public void setTarget3(double target3) { this.target3 = target3; }
        public String getAsset() { return asset; }
        public void setAsset(String asset) { this.asset = asset; }
        public Timeframe getTimeframe() { return timeframe; }
        public void setTimeframe(Timeframe timeframe) { this.timeframe = timeframe; }
        public String getInvalidationCondition() { return invalidationCondition; }
        public void setInvalidationCondition(String invalidationCondition) { this.invalidationCondition = invalidationCondition; }
        public String getSignalValidity() { return signalValidity; }
        public void setSignalValidity(String signalValidity) { this.signalValidity = signalValidity; }
        public String getTechnicalRationale() { return technicalRationale; }
        public void setTechnicalRationale(String technicalRationale) { this.technicalRationale = technicalRationale; }
        public String getFundamentalContext() { return fundamentalContext; }
        public void setFundamentalContext(String fundamentalContext) { this.fundamentalContext = fundamentalContext; }
        public String getMt5Symbol() { return mt5Symbol; }
        public void setMt5Symbol(String mt5Symbol) { this.mt5Symbol = mt5Symbol; }
        public String getMt5OrderType() { return mt5OrderType; }
        public void setMt5OrderType(String mt5OrderType) { this.mt5OrderType = mt5OrderType; }
    }

    /**
     * Generate a trade signal from the provided inputs, executing all
     * analytical layers in sequence.
     *
     * @param input the signal generation inputs
     * @return the generated trade signal
     */
    public TradeSignal generate(SignalInput input) {
        MarketData current = input.getCurrentData();
        AssetClass assetClass = AssetClass.fromSymbol(input.getAsset());

        // Layer 1: Market Regime Classification
        MarketRegime regime = regimeClassifier.classifyRegime(current, input.isCrisis());
        VolatilityRegime volRegime = regimeClassifier.classifyVolatility(current);

        // Layer 2: Technical Analysis
        double htfAlignment = technicalAnalyzer.assessHtfTrendAlignment(
                input.getHtfData(), input.getProposedDirection());
        double momentum = technicalAnalyzer.assessMomentumAlignment(
                current, input.getProposedDirection());
        double volume = technicalAnalyzer.assessVolumeConfirmation(
                current, input.getProposedDirection(), input.getAverageVolume());

        // Structural level confluence
        double structuralLevel = assessStructuralLevel(current, input);

        // Layer 3: Candlestick Pattern Recognition
        List<CandlestickPatternRecognizer.PatternResult> patterns =
                patternRecognizer.detectPatterns(
                        input.getRecentCandles(),
                        input.getSupportLevel(),
                        input.getResistanceLevel(),
                        input.getAtrTolerance());
        double candlestick = patternRecognizer.calculateConfirmationScore(
                patterns, input.getProposedDirection());

        // Layer 4: Fundamental Analysis
        double fundamentals = fundamentalAnalyzer.assessFundamentalSupport(
                assetClass, input.getProposedDirection(),
                input.isDxyTrending(), input.isRealYieldsRising(),
                current.getFundingRate(), input.isInventoryBullish());

        // Layer 5: Sentiment Analysis
        double sentiment = sentimentAnalyzer.assessSentiment(
                input.getProposedDirection(),
                input.getFearGreedIndex(),
                input.getPutCallRatio(),
                input.getCotNetPositioning());

        // Risk-reward check
        RiskMatrix riskMatrix = buildRiskMatrix(input);
        boolean rrAchievable = riskMatrix.meetsMinRiskReward(
                input.getTarget1(),
                input.getProposedDirection() == SignalType.LONG);

        // News event check
        boolean newsNear = fundamentalAnalyzer.isHighImpactEventNear(
                input.getMinutesToNextEvent());

        // Layer 6: Confluence Scoring
        ConfluenceScore confluenceScore = confluenceScorer.score(
                htfAlignment, structuralLevel, candlestick, volume,
                momentum, regime, input.getProposedDirection(),
                fundamentals, sentiment, rrAchievable, newsNear);

        // Build the trade signal
        TradeSignal signal = new TradeSignal();
        signal.setAsset(input.getAsset());
        signal.setAssetClass(assetClass);
        signal.setTimeframe(input.getTimeframe());
        signal.setConfluenceScore(confluenceScore);
        signal.setMarketRegime(regime);
        signal.setVolatilityRegime(volRegime);
        signal.setRiskMatrix(riskMatrix);

        // Determine signal type based on confluence threshold
        if (confluenceScore.meetsSignalThreshold() && rrAchievable) {
            signal.setSignalType(input.getProposedDirection());
        } else {
            signal.setSignalType(SignalType.STAND_ASIDE);
        }

        // Populate signal details
        signal.setEntryLow(input.getEntryLow());
        signal.setEntryHigh(input.getEntryHigh());
        signal.setStopLoss(input.getStopLoss());
        signal.setStopLossBasis(input.getStopLossBasis());
        signal.setTarget1(input.getTarget1());
        signal.setTarget2(input.getTarget2());
        signal.setTarget3(input.getTarget3());
        signal.setInvalidationCondition(input.getInvalidationCondition());
        signal.setSignalValidity(input.getSignalValidity());
        signal.setTechnicalRationale(input.getTechnicalRationale());
        signal.setFundamentalContext(input.getFundamentalContext());
        signal.setMt5Symbol(input.getMt5Symbol());
        signal.setMt5OrderType(input.getMt5OrderType());

        // Win probability estimate based on confluence
        signal.setWinProbabilityPercent(estimateWinProbability(confluenceScore));

        // Apply position size reduction if near high-impact event
        if (fundamentalAnalyzer.shouldReducePositionSize(input.getMinutesToNextEvent())) {
            double reducedRisk = riskMatrix.getAccountRiskPercent() * 0.5;
            riskMatrix.setAccountRiskPercent(reducedRisk);
        }

        return signal;
    }

    /**
     * Generate a signal and return the formatted output string.
     */
    public String generateFormatted(SignalInput input) {
        TradeSignal signal = generate(input);
        return formatter.format(signal);
    }

    private double assessStructuralLevel(MarketData current, SignalInput input) {
        double price = current.getClose();
        double distToSupport = Math.abs(price - input.getSupportLevel());
        double distToResistance = Math.abs(price - input.getResistanceLevel());
        double minDist = Math.min(distToSupport, distToResistance);

        if (input.getAtrTolerance() <= 0) {
            return 0.0;
        }

        // Score based on proximity to structural level
        double ratio = minDist / input.getAtrTolerance();
        if (ratio <= 0.5) {
            return 1.0;
        }
        if (ratio <= 1.0) {
            return 0.7;
        }
        if (ratio <= 2.0) {
            return 0.3;
        }
        return 0.0;
    }

    private RiskMatrix buildRiskMatrix(SignalInput input) {
        RiskMatrix rm = new RiskMatrix();
        rm.setAccountBalance(input.getAccountBalance());
        rm.setAccountRiskPercent(input.getAccountRiskPercent() > 0
                ? input.getAccountRiskPercent() : RiskMatrix.DEFAULT_ACCOUNT_RISK_PERCENT);
        double midEntry = (input.getEntryLow() + input.getEntryHigh()) / 2.0;
        rm.setEntryPrice(midEntry);
        rm.setStopLossPrice(input.getStopLoss());

        // Determine risk level
        double rr = rm.getRiskRewardRatio(input.getTarget1(),
                input.getProposedDirection() == SignalType.LONG);
        if (rr >= 3.0) {
            rm.setRiskLevel(RiskLevel.LOW);
            rm.setRiskJustification("Favorable R:R >= 3:1");
        } else if (rr >= 2.0) {
            rm.setRiskLevel(RiskLevel.MEDIUM);
            rm.setRiskJustification("Acceptable R:R >= 2:1");
        } else if (rr >= 1.0) {
            rm.setRiskLevel(RiskLevel.HIGH);
            rm.setRiskJustification("Sub-optimal R:R < 2:1");
        } else {
            rm.setRiskLevel(RiskLevel.EXTREME);
            rm.setRiskJustification("Unfavorable R:R < 1:1");
        }

        return rm;
    }

    private int estimateWinProbability(ConfluenceScore cs) {
        double total = cs.getTotal();
        if (total >= 9.0) return 75;
        if (total >= 8.0) return 65;
        if (total >= 7.0) return 55;
        if (total >= 6.0) return 45;
        return 35;
    }
}
