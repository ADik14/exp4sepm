package com.example.marketanalysis.model;

/**
 * OHLCV (Open, High, Low, Close, Volume) market data for a single candle.
 */
public class MarketData {

    private String symbol;
    private Timeframe timeframe;
    private long timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    // Derived technical indicators (pre-computed or supplied externally)
    private double adx;
    private double atr;
    private double averageAtr;
    private double rsi;
    private double macdHistogram;
    private double stochK;
    private double stochD;
    private double vwap;
    private double ema9;
    private double ema21;
    private double ema50;
    private double ema200;

    // Funding rate (crypto specific)
    private double fundingRate;

    public MarketData() {
    }

    /** Calculate the body size of the candle. */
    public double getBodySize() {
        return Math.abs(close - open);
    }

    /** Calculate the upper wick size. */
    public double getUpperWick() {
        return high - Math.max(open, close);
    }

    /** Calculate the lower wick size. */
    public double getLowerWick() {
        return Math.min(open, close) - low;
    }

    /** Check if the candle is bullish (close > open). */
    public boolean isBullish() {
        return close > open;
    }

    /** Check if the candle is bearish (close < open). */
    public boolean isBearish() {
        return close < open;
    }

    /** Check if this is a doji (body < 10% of total range). */
    public boolean isDoji() {
        double range = high - low;
        return range > 0 && getBodySize() / range < 0.1;
    }

    // --- Getters and Setters ---

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Timeframe getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(Timeframe timeframe) {
        this.timeframe = timeframe;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getAdx() {
        return adx;
    }

    public void setAdx(double adx) {
        this.adx = adx;
    }

    public double getAtr() {
        return atr;
    }

    public void setAtr(double atr) {
        this.atr = atr;
    }

    public double getAverageAtr() {
        return averageAtr;
    }

    public void setAverageAtr(double averageAtr) {
        this.averageAtr = averageAtr;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public double getMacdHistogram() {
        return macdHistogram;
    }

    public void setMacdHistogram(double macdHistogram) {
        this.macdHistogram = macdHistogram;
    }

    public double getStochK() {
        return stochK;
    }

    public void setStochK(double stochK) {
        this.stochK = stochK;
    }

    public double getStochD() {
        return stochD;
    }

    public void setStochD(double stochD) {
        this.stochD = stochD;
    }

    public double getVwap() {
        return vwap;
    }

    public void setVwap(double vwap) {
        this.vwap = vwap;
    }

    public double getEma9() {
        return ema9;
    }

    public void setEma9(double ema9) {
        this.ema9 = ema9;
    }

    public double getEma21() {
        return ema21;
    }

    public void setEma21(double ema21) {
        this.ema21 = ema21;
    }

    public double getEma50() {
        return ema50;
    }

    public void setEma50(double ema50) {
        this.ema50 = ema50;
    }

    public double getEma200() {
        return ema200;
    }

    public void setEma200(double ema200) {
        this.ema200 = ema200;
    }

    public double getFundingRate() {
        return fundingRate;
    }

    public void setFundingRate(double fundingRate) {
        this.fundingRate = fundingRate;
    }
}
