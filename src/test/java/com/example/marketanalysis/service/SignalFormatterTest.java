package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignalFormatterTest {

    private SignalFormatter formatter;

    @Before
    public void setUp() {
        formatter = new SignalFormatter();
    }

    @Test
    public void testFormatNullSignal() {
        assertEquals("No signal generated.", formatter.format(null));
    }

    @Test
    public void testFormatStandAsideSignal() {
        TradeSignal signal = new TradeSignal();
        signal.setAsset("BTCUSD");
        signal.setSignalType(SignalType.STAND_ASIDE);
        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(0.5);
        signal.setConfluenceScore(cs);

        String output = formatter.format(signal);
        assertTrue(output.contains("STAND ASIDE"));
        assertTrue(output.contains("BTCUSD"));
        assertTrue(output.contains("informational and educational purposes"));
    }

    @Test
    public void testFormatLongSignalContainsAllSections() {
        TradeSignal signal = buildSampleSignal(SignalType.LONG);
        String output = formatter.format(signal);

        assertTrue(output.contains("▲ LONG"));
        assertTrue(output.contains("ENTRY ZONE"));
        assertTrue(output.contains("STOP LOSS"));
        assertTrue(output.contains("TARGET 1"));
        assertTrue(output.contains("TARGET 2"));
        assertTrue(output.contains("TARGET 3"));
        assertTrue(output.contains("SIGNAL VALIDITY"));
        assertTrue(output.contains("INVALIDATION"));
        assertTrue(output.contains("TRADER PROFILES"));
        assertTrue(output.contains("RISK MATRIX"));
        assertTrue(output.contains("CONFLUENCE BREAKDOWN"));
        assertTrue(output.contains("TECHNICAL RATIONALE"));
        assertTrue(output.contains("FUNDAMENTAL CONTEXT"));
        assertTrue(output.contains("MT5 EXECUTION NOTE"));
    }

    @Test
    public void testFormatContainsDisclaimer() {
        TradeSignal signal = buildSampleSignal(SignalType.LONG);
        String output = formatter.format(signal);
        assertTrue(output.contains("does not constitute financial"));
    }

    private TradeSignal buildSampleSignal(SignalType type) {
        TradeSignal signal = new TradeSignal();
        signal.setAsset("XAUUSD");
        signal.setTimeframe(Timeframe.H4);
        signal.setSignalType(type);
        signal.setWinProbabilityPercent(65);
        signal.setEntryLow(2350.0);
        signal.setEntryHigh(2355.0);
        signal.setStopLoss(2340.0);
        signal.setStopLossBasis("1.5x ATR(14)");
        signal.setTarget1(2370.0);
        signal.setTarget2(2385.0);
        signal.setTarget3(2400.0);
        signal.setSignalValidity("Valid for 24 hours");
        signal.setInvalidationCondition("4H close below 2340");
        signal.setScalperNotes("Tight SL, TP1 only during London session");
        signal.setIntradayNotes("Avoid NY open volatility");
        signal.setSwingNotes("Trail stop at 1x ATR after TP1");
        signal.setTechnicalRationale("Bullish structure on daily.");
        signal.setFundamentalContext("DXY weakening, real yields falling.");
        signal.setMt5Symbol("XAUUSD");
        signal.setMt5OrderType("LIMIT");
        signal.setMt5PendingPrice("2352.50");
        signal.setMt5Expiry("End of London session");

        ConfluenceScore cs = new ConfluenceScore();
        cs.setHtfTrendAlignment(1.0);
        cs.setStructuralLevelConfluence(0.8);
        cs.setCandlestickConfirmation(0.7);
        cs.setVolumeConfirmation(0.9);
        cs.setMomentumAlignment(0.8);
        cs.setRegimeCompatibility(1.0);
        cs.setFundamentalSupport(0.7);
        cs.setSentimentPositioning(0.6);
        cs.setRiskRewardAchievable(1.0);
        cs.setNoMajorNewsEvent(1.0);
        signal.setConfluenceScore(cs);

        RiskMatrix rm = new RiskMatrix();
        rm.setAccountBalance(100000.0);
        rm.setAccountRiskPercent(1.0);
        rm.setEntryPrice(2352.5);
        rm.setStopLossPrice(2340.0);
        rm.setRiskLevel(RiskLevel.MEDIUM);
        rm.setRiskJustification("Acceptable R:R");
        signal.setRiskMatrix(rm);

        return signal;
    }
}
