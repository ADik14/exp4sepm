package com.example.marketanalysis;

import com.example.marketanalysis.model.*;
import com.example.marketanalysis.service.SignalGenerator;
import com.example.marketanalysis.service.SignalGenerator.SignalInput;

import java.util.Arrays;

/**
 * Main entry point for the Quantitative Market Analysis Agent.
 * Demonstrates signal generation with sample market data.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Quantitative Market Analysis Agent v2.0 ===");
        System.out.println();

        SignalGenerator generator = new SignalGenerator();

        // Build sample market data for demonstration
        SignalInput input = buildSampleInput();

        // Generate and print formatted signal
        String output = generator.generateFormatted(input);
        System.out.println(output);
    }

    /**
     * Build a sample input for demonstration purposes.
     * In production, this data would come from a live market feed or MT5.
     */
    private static SignalInput buildSampleInput() {
        // Current candle data (4H BTCUSD)
        MarketData current = new MarketData();
        current.setSymbol("BTCUSD");
        current.setTimeframe(Timeframe.H4);
        current.setOpen(67500.0);
        current.setHigh(68200.0);
        current.setLow(67300.0);
        current.setClose(68050.0);
        current.setVolume(15000);
        current.setAdx(28.5);       // Trending regime
        current.setAtr(450.0);
        current.setAverageAtr(400.0);
        current.setRsi(58.0);
        current.setMacdHistogram(120.0);
        current.setStochK(65.0);
        current.setStochD(55.0);
        current.setVwap(67800.0);
        current.setEma9(67900.0);
        current.setEma21(67500.0);
        current.setEma50(66800.0);
        current.setEma200(63500.0);
        current.setFundingRate(0.0005);

        // Previous candle (for pattern detection)
        MarketData previous = new MarketData();
        previous.setSymbol("BTCUSD");
        previous.setTimeframe(Timeframe.H4);
        previous.setOpen(67800.0);
        previous.setHigh(67900.0);
        previous.setLow(67200.0);
        previous.setClose(67500.0);
        previous.setVolume(12000);

        // HTF data (Daily)
        MarketData dailyData = new MarketData();
        dailyData.setSymbol("BTCUSD");
        dailyData.setTimeframe(Timeframe.DAILY);
        dailyData.setClose(68050.0);
        dailyData.setEma9(67800.0);
        dailyData.setEma21(67200.0);
        dailyData.setEma50(66000.0);
        dailyData.setEma200(62000.0);

        // Build signal input
        SignalInput input = new SignalInput();
        input.setCurrentData(current);
        input.setRecentCandles(Arrays.asList(previous, current));
        input.setHtfData(Arrays.asList(dailyData));
        input.setAsset("BTCUSD");
        input.setTimeframe(Timeframe.H4);
        input.setProposedDirection(SignalType.LONG);

        // Structural levels
        input.setSupportLevel(67000.0);
        input.setResistanceLevel(69500.0);
        input.setAtrTolerance(450.0);
        input.setAverageVolume(13000.0);

        // Trade levels
        input.setEntryLow(67300.0);
        input.setEntryHigh(67500.0);
        input.setStopLoss(67000.0);
        input.setStopLossBasis("1.5x ATR(14) below entry zone");
        input.setTarget1(68200.0);
        input.setTarget2(69000.0);
        input.setTarget3(70000.0);

        // Fundamental inputs
        input.setDxyTrending(false);
        input.setRealYieldsRising(false);
        input.setInventoryBullish(true);
        input.setCrisis(false);
        input.setMinutesToNextEvent(480); // 8 hours to next event

        // Sentiment inputs
        input.setFearGreedIndex(45); // Neutral-fear
        input.setPutCallRatio(0.85);
        input.setCotNetPositioning(-5000);

        // Risk inputs
        input.setAccountBalance(100000.0);
        input.setAccountRiskPercent(1.0);

        // Metadata
        input.setInvalidationCondition("4H close below 67000 (support break)");
        input.setSignalValidity("Valid for next 3x 4H candles or until invalidated");
        input.setTechnicalRationale(
                "Daily trend bullish with EMA stack aligned (9>21>50>200). "
                + "4H showing higher highs/lows with bullish engulfing at 67300 support. "
                + "RSI at 58 with room to run, MACD histogram expanding positive.");
        input.setFundamentalContext(
                "BTC funding rate neutral at 0.05%, no extreme positioning. "
                + "No major macro events within 8 hours.");
        input.setMt5Symbol("BTCUSD");
        input.setMt5OrderType("LIMIT");

        return input;
    }
}
