package com.example.marketanalysis.service;

import com.example.marketanalysis.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SignalGeneratorTest {

    private SignalGenerator generator;

    @Before
    public void setUp() {
        generator = new SignalGenerator();
    }

    @Test
    public void testGenerateActionableSignal() {
        SignalGenerator.SignalInput input = buildHighConfluenceInput();
        TradeSignal signal = generator.generate(input);

        assertNotNull(signal);
        assertEquals(SignalType.LONG, signal.getSignalType());
        assertTrue(signal.isActionable());
        assertNotNull(signal.getConfluenceScore());
        assertTrue(signal.getConfluenceScore().getTotal() >= 7.0);
    }

    @Test
    public void testGenerateStandAsideForLowConfluence() {
        SignalGenerator.SignalInput input = buildLowConfluenceInput();
        TradeSignal signal = generator.generate(input);

        assertNotNull(signal);
        assertEquals(SignalType.STAND_ASIDE, signal.getSignalType());
        assertFalse(signal.isActionable());
    }

    @Test
    public void testFormattedOutputContainsDisclaimer() {
        SignalGenerator.SignalInput input = buildHighConfluenceInput();
        String output = generator.generateFormatted(input);

        assertNotNull(output);
        assertTrue("Output should contain disclaimer",
                output.contains("informational and educational purposes only"));
    }

    @Test
    public void testFormattedOutputContainsAsset() {
        SignalGenerator.SignalInput input = buildHighConfluenceInput();
        String output = generator.generateFormatted(input);

        assertTrue("Output should contain asset name",
                output.contains("BTCUSD"));
    }

    @Test
    public void testPositionSizeReducedNearEvent() {
        SignalGenerator.SignalInput input = buildHighConfluenceInput();
        input.setMinutesToNextEvent(60); // 1 hour to event

        TradeSignal signal = generator.generate(input);

        // Position size should be reduced by 50%
        assertEquals(0.5, signal.getRiskMatrix().getAccountRiskPercent(), 0.001);
    }

    @Test
    public void testInvalidationRequired() {
        SignalGenerator.SignalInput input = buildHighConfluenceInput();
        input.setInvalidationCondition(null);

        TradeSignal signal = generator.generate(input);

        // Without invalidation, signal should not be actionable
        assertFalse("Signal without invalidation should not be actionable",
                signal.isActionable());
    }

    private SignalGenerator.SignalInput buildHighConfluenceInput() {
        // Build a hammer candle at support for candlestick confirmation
        MarketData current = new MarketData();
        current.setSymbol("BTCUSD");
        current.setTimeframe(Timeframe.H4);
        current.setOpen(67400.0);
        current.setHigh(67500.0);
        current.setLow(66800.0);     // Long lower wick at support
        current.setClose(67450.0);   // Small body, big lower wick = hammer
        current.setVolume(15000);
        current.setAdx(28.5);
        current.setAtr(450.0);
        current.setAverageAtr(400.0);
        current.setRsi(58.0);
        current.setMacdHistogram(120.0);
        current.setStochK(65.0);
        current.setStochD(55.0);
        current.setVwap(67200.0);    // Price above VWAP
        current.setEma9(67300.0);
        current.setEma21(67000.0);
        current.setEma50(66500.0);
        current.setEma200(63500.0);
        current.setFundingRate(-0.001); // Negative funding = shorts at risk = supports long

        MarketData dailyData = new MarketData();
        dailyData.setClose(67450.0);
        dailyData.setEma9(67200.0);
        dailyData.setEma21(66800.0);
        dailyData.setEma50(66000.0);
        dailyData.setEma200(62000.0);

        SignalGenerator.SignalInput input = new SignalGenerator.SignalInput();
        input.setCurrentData(current);
        input.setRecentCandles(Arrays.asList(current));
        input.setHtfData(Arrays.asList(dailyData));
        input.setAsset("BTCUSD");
        input.setTimeframe(Timeframe.H4);
        input.setProposedDirection(SignalType.LONG);
        input.setSupportLevel(66900.0);   // Near the hammer's low
        input.setResistanceLevel(69500.0);
        input.setAtrTolerance(450.0);
        input.setAverageVolume(13000.0);
        input.setEntryLow(67100.0);
        input.setEntryHigh(67300.0);
        input.setStopLoss(66800.0);        // SL below support
        input.setStopLossBasis("1.5x ATR(14)");
        input.setTarget1(68200.0);         // R:R = 2.5:1
        input.setTarget2(70500.0);
        input.setTarget3(72000.0);
        input.setDxyTrending(false);
        input.setRealYieldsRising(false);
        input.setInventoryBullish(true);
        input.setCrisis(false);
        input.setMinutesToNextEvent(480);
        input.setFearGreedIndex(20);       // Extreme fear = contrarian bullish
        input.setPutCallRatio(1.3);        // High P/C = contrarian bullish
        input.setCotNetPositioning(-5000); // Net short = contrarian bullish
        input.setAccountBalance(100000.0);
        input.setAccountRiskPercent(1.0);
        input.setInvalidationCondition("4H close below 67000");
        input.setSignalValidity("Valid for next 3x 4H candles");
        input.setTechnicalRationale("Bullish EMA stack on daily.");
        input.setFundamentalContext("Neutral funding rate.");
        input.setMt5Symbol("BTCUSD");
        input.setMt5OrderType("LIMIT");

        return input;
    }

    private SignalGenerator.SignalInput buildLowConfluenceInput() {
        MarketData current = new MarketData();
        current.setSymbol("BTCUSD");
        current.setTimeframe(Timeframe.H4);
        current.setOpen(67500.0);
        current.setHigh(68200.0);
        current.setLow(67300.0);
        current.setClose(67400.0); // Bearish close contradicts LONG
        current.setVolume(8000);   // Below average
        current.setAdx(18.0);     // Range-bound
        current.setAtr(200.0);
        current.setAverageAtr(400.0);
        current.setRsi(45.0);
        current.setMacdHistogram(-50.0); // Bearish MACD
        current.setStochK(40.0);
        current.setStochD(50.0);  // Stoch bearish cross
        current.setVwap(67800.0);
        current.setEma9(67600.0);
        current.setEma21(67700.0); // EMA 9 < 21 = bearish
        current.setEma50(67800.0);
        current.setEma200(67900.0);
        current.setFundingRate(0.002); // Extreme positive = longs at risk

        SignalGenerator.SignalInput input = new SignalGenerator.SignalInput();
        input.setCurrentData(current);
        input.setRecentCandles(Arrays.asList(current));
        input.setHtfData(Arrays.asList(current));
        input.setAsset("BTCUSD");
        input.setTimeframe(Timeframe.H4);
        input.setProposedDirection(SignalType.LONG);
        input.setSupportLevel(65000.0);
        input.setResistanceLevel(70000.0);
        input.setAtrTolerance(200.0);
        input.setAverageVolume(13000.0);
        input.setEntryLow(67300.0);
        input.setEntryHigh(67500.0);
        input.setStopLoss(67000.0);
        input.setStopLossBasis("1.5x ATR(14)");
        input.setTarget1(67800.0); // Poor R:R
        input.setTarget2(68000.0);
        input.setTarget3(68500.0);
        input.setCrisis(false);
        input.setMinutesToNextEvent(15); // News very near
        input.setFearGreedIndex(85);     // Extreme greed = longs crowded
        input.setPutCallRatio(0.5);
        input.setCotNetPositioning(10000);
        input.setAccountBalance(100000.0);
        input.setAccountRiskPercent(1.0);
        input.setInvalidationCondition("Below 67000");

        return input;
    }
}
