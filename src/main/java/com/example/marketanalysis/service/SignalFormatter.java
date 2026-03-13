package com.example.marketanalysis.service;

import com.example.marketanalysis.model.ConfluenceScore;
import com.example.marketanalysis.model.RiskMatrix;
import com.example.marketanalysis.model.SignalType;
import com.example.marketanalysis.model.TradeSignal;

/**
 * Formats a {@link TradeSignal} into the structured output block
 * required by the signal output specification.
 */
public class SignalFormatter {

    private static final String SEPARATOR =
            "═══════════════════════════════════════════════════════";
    private static final String LINE =
            "───────────────────────────────────────────────────────";

    /**
     * Format a complete trade signal into the structured output string.
     *
     * @param signal the trade signal to format
     * @return the formatted signal block
     */
    public String format(TradeSignal signal) {
        if (signal == null) {
            return "No signal generated.";
        }

        // Stand-aside signals use a simplified format
        if (signal.getSignalType() == SignalType.STAND_ASIDE) {
            return formatStandAside(signal);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATOR).append('\n');
        appendHeader(sb, signal);
        sb.append(LINE).append('\n');
        appendLevels(sb, signal);
        sb.append(LINE).append('\n');
        appendValidity(sb, signal);
        sb.append(LINE).append('\n');
        appendTraderProfiles(sb, signal);
        sb.append(LINE).append('\n');
        appendRiskMatrix(sb, signal);
        sb.append(LINE).append('\n');
        appendConfluenceBreakdown(sb, signal);
        sb.append(LINE).append('\n');
        appendRationale(sb, signal);
        sb.append(LINE).append('\n');
        appendMt5Execution(sb, signal);
        sb.append(SEPARATOR).append('\n');
        sb.append('\n');
        appendDisclaimer(sb);
        return sb.toString();
    }

    private String formatStandAside(TradeSignal signal) {
        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATOR).append('\n');
        sb.append(String.format("ASSET:         %s%n", safe(signal.getAsset())));
        sb.append(String.format("SIGNAL:        %s%n", SignalType.STAND_ASIDE.getDisplay()));
        if (signal.getConfluenceScore() != null) {
            sb.append(String.format("CONFIDENCE:    %.1f / 10%n",
                    signal.getConfluenceScore().getTotal()));
            sb.append(String.format("VERDICT:       %s%n",
                    signal.getConfluenceScore().getVerdict()));
        }
        sb.append(SEPARATOR).append('\n');
        sb.append('\n');
        appendDisclaimer(sb);
        return sb.toString();
    }

    private void appendHeader(StringBuilder sb, TradeSignal signal) {
        sb.append(String.format("ASSET:         %s%n", safe(signal.getAsset())));
        sb.append(String.format("TIMEFRAME:     %s%n",
                signal.getTimeframe() != null ? signal.getTimeframe().getLabel() : "N/A"));
        sb.append(String.format("SIGNAL:        %s%n", signal.getSignalType().getDisplay()));
        sb.append(String.format("CONFIDENCE:    %s  |  WIN PROBABILITY: ~%d%%%n",
                signal.getConfidenceDisplay(), signal.getWinProbabilityPercent()));
    }

    private void appendLevels(StringBuilder sb, TradeSignal signal) {
        sb.append(String.format("ENTRY ZONE:    %.2f — %.2f%n",
                signal.getEntryLow(), signal.getEntryHigh()));
        sb.append(String.format("STOP LOSS:     %.2f — %s%n",
                signal.getStopLoss(), safe(signal.getStopLossBasis())));

        boolean isLong = signal.getSignalType() == SignalType.LONG;
        RiskMatrix rm = signal.getRiskMatrix();

        sb.append(String.format("TARGET 1:      %.2f — partial exit [50%% position] — R:R %.1f:1%n",
                signal.getTarget1(),
                rm != null ? rm.getRiskRewardRatio(signal.getTarget1(), isLong) : 0));
        sb.append(String.format("TARGET 2:      %.2f — partial exit [30%% position] — R:R %.1f:1%n",
                signal.getTarget2(),
                rm != null ? rm.getRiskRewardRatio(signal.getTarget2(), isLong) : 0));
        sb.append(String.format("TARGET 3:      %.2f — runner [20%% position] — R:R %.1f:1%n",
                signal.getTarget3(),
                rm != null ? rm.getRiskRewardRatio(signal.getTarget3(), isLong) : 0));
    }

    private void appendValidity(StringBuilder sb, TradeSignal signal) {
        sb.append(String.format("SIGNAL VALIDITY: %s%n", safe(signal.getSignalValidity())));
        sb.append(String.format("INVALIDATION:  %s%n", safe(signal.getInvalidationCondition())));
    }

    private void appendTraderProfiles(StringBuilder sb, TradeSignal signal) {
        sb.append("TRADER PROFILES:\n");
        sb.append(String.format("  SCALPER     (1M-15M): %s%n", safe(signal.getScalperNotes())));
        sb.append(String.format("  INTRADAY    (15M-4H): %s%n", safe(signal.getIntradayNotes())));
        sb.append(String.format("  SWING       (4H-1W):  %s%n", safe(signal.getSwingNotes())));
    }

    private void appendRiskMatrix(StringBuilder sb, TradeSignal signal) {
        sb.append("RISK MATRIX:\n");
        RiskMatrix rm = signal.getRiskMatrix();
        if (rm != null) {
            sb.append(String.format("  Account Risk:     %.1f%% max per trade%n",
                    rm.getAccountRiskPercent()));
            sb.append(String.format("  Position Size:    Risk $%.2f ÷ (%.2f - %.2f) = %.4f lots%n",
                    rm.getRiskDollars(), rm.getEntryPrice(), rm.getStopLossPrice(),
                    rm.getPositionSize()));
            sb.append(String.format("  Max Drawdown:     %.1f%% of account at %.1f%% risk%n",
                    rm.getMaxDrawdownPercent(), rm.getAccountRiskPercent()));
            sb.append(String.format("  RISK LEVEL:       %s — %s%n",
                    rm.getRiskLevel() != null ? rm.getRiskLevel().getDisplay() : "N/A",
                    safe(rm.getRiskJustification())));
        } else {
            sb.append("  N/A\n");
        }
    }

    private void appendConfluenceBreakdown(StringBuilder sb, TradeSignal signal) {
        sb.append("CONFLUENCE BREAKDOWN:\n");
        ConfluenceScore cs = signal.getConfluenceScore();
        if (cs != null) {
            sb.append(String.format("  Score: %.1f/10%n", cs.getTotal()));
            sb.append(String.format("  HTF Trend Alignment:          %.1f%n",
                    cs.getHtfTrendAlignment()));
            sb.append(String.format("  Structural Level Confluence:  %.1f%n",
                    cs.getStructuralLevelConfluence()));
            sb.append(String.format("  Candlestick Confirmation:     %.1f%n",
                    cs.getCandlestickConfirmation()));
            sb.append(String.format("  Volume Confirmation:          %.1f%n",
                    cs.getVolumeConfirmation()));
            sb.append(String.format("  Momentum Alignment:           %.1f%n",
                    cs.getMomentumAlignment()));
            sb.append(String.format("  Regime Compatibility:         %.1f%n",
                    cs.getRegimeCompatibility()));
            sb.append(String.format("  Fundamental Support:          %.1f%n",
                    cs.getFundamentalSupport()));
            sb.append(String.format("  Sentiment Positioning:        %.1f%n",
                    cs.getSentimentPositioning()));
            sb.append(String.format("  Risk/Reward Achievable:       %.1f%n",
                    cs.getRiskRewardAchievable()));
            sb.append(String.format("  No Major News Event:          %.1f%n",
                    cs.getNoMajorNewsEvent()));
        }
    }

    private void appendRationale(StringBuilder sb, TradeSignal signal) {
        sb.append("TECHNICAL RATIONALE:\n");
        sb.append(String.format("  %s%n", safe(signal.getTechnicalRationale())));
        sb.append("FUNDAMENTAL CONTEXT:\n");
        sb.append(String.format("  %s%n", safe(signal.getFundamentalContext())));
    }

    private void appendMt5Execution(StringBuilder sb, TradeSignal signal) {
        sb.append("MT5 EXECUTION NOTE:\n");
        sb.append(String.format("  Order Type:     %s%n", safe(signal.getMt5OrderType())));
        sb.append(String.format("  MT5 Symbol:     %s%n", safe(signal.getMt5Symbol())));
        sb.append(String.format("  Pending Order:  %s%n", safe(signal.getMt5PendingPrice())));
        sb.append(String.format("  Expiry:         %s%n", safe(signal.getMt5Expiry())));
    }

    private void appendDisclaimer(StringBuilder sb) {
        sb.append("⚠ This analysis is generated by an AI quantitative model and is provided for\n");
        sb.append("  informational and educational purposes only. It does not constitute financial\n");
        sb.append("  advice. Past signal accuracy does not guarantee future results. All trading\n");
        sb.append("  involves substantial risk of loss. Never risk capital you cannot afford to lose.\n");
        sb.append("  Verify all levels on your live MT5 chart before execution.\n");
    }

    private static String safe(String value) {
        return value != null ? value : "N/A";
    }
}
