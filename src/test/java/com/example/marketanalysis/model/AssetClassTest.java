package com.example.marketanalysis.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AssetClassTest {

    @Test
    public void testCryptoSymbols() {
        assertEquals(AssetClass.CRYPTO, AssetClass.fromSymbol("BTCUSD"));
        assertEquals(AssetClass.CRYPTO, AssetClass.fromSymbol("ETHUSD"));
        assertEquals(AssetClass.CRYPTO, AssetClass.fromSymbol("BTC/USDT"));
    }

    @Test
    public void testPreciousMetalsSymbols() {
        assertEquals(AssetClass.PRECIOUS_METALS, AssetClass.fromSymbol("XAUUSD"));
        assertEquals(AssetClass.PRECIOUS_METALS, AssetClass.fromSymbol("XAGUSD"));
    }

    @Test
    public void testCommodityFuturesDefault() {
        assertEquals(AssetClass.COMMODITY_FUTURES, AssetClass.fromSymbol("CL1!"));
        assertEquals(AssetClass.COMMODITY_FUTURES, AssetClass.fromSymbol("NG"));
        assertEquals(AssetClass.COMMODITY_FUTURES, AssetClass.fromSymbol("ZC"));
    }
}
