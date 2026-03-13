package com.example.marketanalysis.model;

/**
 * Risk matrix for a trade signal, including position sizing calculations.
 * Position size formula: Risk$ ÷ (Entry − SL) = Lot/Contract size.
 */
public class RiskMatrix {

    /** Default maximum account risk per trade (1-2%). */
    public static final double DEFAULT_ACCOUNT_RISK_PERCENT = 1.0;

    /** Minimum acceptable risk-to-reward ratio. */
    public static final double MIN_RISK_REWARD_RATIO = 2.0;

    private double accountRiskPercent;
    private double accountBalance;
    private double entryPrice;
    private double stopLossPrice;
    private RiskLevel riskLevel;
    private String riskJustification;

    public RiskMatrix() {
        this.accountRiskPercent = DEFAULT_ACCOUNT_RISK_PERCENT;
    }

    /**
     * Calculate the dollar risk amount.
     */
    public double getRiskDollars() {
        return accountBalance * (accountRiskPercent / 100.0);
    }

    /**
     * Calculate the position size (lots/contracts) using the formula:
     * Risk$ ÷ |Entry − SL|
     */
    public double getPositionSize() {
        double slDistance = Math.abs(entryPrice - stopLossPrice);
        if (slDistance == 0) {
            return 0;
        }
        return getRiskDollars() / slDistance;
    }

    /**
     * Calculate the maximum drawdown percentage if stop loss is hit.
     */
    public double getMaxDrawdownPercent() {
        return accountRiskPercent;
    }

    /**
     * Calculate the risk-to-reward ratio for a given target price.
     *
     * @param targetPrice the target exit price
     * @param isLong      true for long trades, false for short trades
     * @return the risk-to-reward ratio
     */
    public double getRiskRewardRatio(double targetPrice, boolean isLong) {
        double risk = Math.abs(entryPrice - stopLossPrice);
        if (risk == 0) {
            return 0;
        }
        double reward;
        if (isLong) {
            reward = targetPrice - entryPrice;
        } else {
            reward = entryPrice - targetPrice;
        }
        return reward / risk;
    }

    /**
     * Check whether a given target achieves the minimum 2:1 risk-reward.
     */
    public boolean meetsMinRiskReward(double targetPrice, boolean isLong) {
        return getRiskRewardRatio(targetPrice, isLong) >= MIN_RISK_REWARD_RATIO;
    }

    // --- Getters and Setters ---

    public double getAccountRiskPercent() {
        return accountRiskPercent;
    }

    public void setAccountRiskPercent(double accountRiskPercent) {
        this.accountRiskPercent = accountRiskPercent;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskJustification() {
        return riskJustification;
    }

    public void setRiskJustification(String riskJustification) {
        this.riskJustification = riskJustification;
    }
}
