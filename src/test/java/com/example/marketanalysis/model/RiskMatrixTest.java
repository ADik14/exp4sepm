package com.example.marketanalysis.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class RiskMatrixTest {

    @Test
    public void testPositionSizeCalculation() {
        RiskMatrix rm = new RiskMatrix();
        rm.setAccountBalance(100000.0);
        rm.setAccountRiskPercent(1.0);
        rm.setEntryPrice(68000.0);
        rm.setStopLossPrice(67000.0);
        // Risk = $1000, SL distance = 1000, position size = 1.0
        assertEquals(1.0, rm.getPositionSize(), 0.001);
    }

    @Test
    public void testRiskDollars() {
        RiskMatrix rm = new RiskMatrix();
        rm.setAccountBalance(50000.0);
        rm.setAccountRiskPercent(2.0);
        assertEquals(1000.0, rm.getRiskDollars(), 0.001);
    }

    @Test
    public void testRiskRewardRatioLong() {
        RiskMatrix rm = new RiskMatrix();
        rm.setEntryPrice(100.0);
        rm.setStopLossPrice(95.0);
        // Risk = 5, Target at 115 = reward 15, R:R = 3:1
        assertEquals(3.0, rm.getRiskRewardRatio(115.0, true), 0.001);
    }

    @Test
    public void testRiskRewardRatioShort() {
        RiskMatrix rm = new RiskMatrix();
        rm.setEntryPrice(100.0);
        rm.setStopLossPrice(105.0);
        // Risk = 5, Target at 90 = reward 10, R:R = 2:1
        assertEquals(2.0, rm.getRiskRewardRatio(90.0, false), 0.001);
    }

    @Test
    public void testMeetsMinRiskReward() {
        RiskMatrix rm = new RiskMatrix();
        rm.setEntryPrice(100.0);
        rm.setStopLossPrice(95.0);
        assertTrue(rm.meetsMinRiskReward(110.0, true));  // R:R = 2:1
        assertFalse(rm.meetsMinRiskReward(104.0, true)); // R:R = 0.8:1
    }

    @Test
    public void testZeroSlDistance() {
        RiskMatrix rm = new RiskMatrix();
        rm.setEntryPrice(100.0);
        rm.setStopLossPrice(100.0);
        assertEquals(0, rm.getPositionSize(), 0.001);
        assertEquals(0, rm.getRiskRewardRatio(110.0, true), 0.001);
    }

    @Test
    public void testDefaultRiskPercent() {
        RiskMatrix rm = new RiskMatrix();
        assertEquals(RiskMatrix.DEFAULT_ACCOUNT_RISK_PERCENT, rm.getAccountRiskPercent(), 0.001);
    }
}
