package com.mizuho.testutils;

import com.mizuho.model.Instrument;
import com.mizuho.model.TradePrice;
import com.mizuho.model.Vendor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TradePriceStub {

    public static TradePrice createTradePrice(String id) {
        return new TradePrice(id, new Vendor(Long.valueOf(3), "TEST_Vendor"),
                new Instrument(Long.valueOf(3), "MS"),
                BigDecimal.valueOf(102.34), BigDecimal.valueOf(103.55), null);
    }

    public static TradePrice createTradePriceByInstrument(String id, String instrumentName) {
        return new TradePrice(id, new Vendor(Long.valueOf(5), "TEST_Vendor"),
                new Instrument(Long.valueOf(6), instrumentName),
                BigDecimal.valueOf(11.34), BigDecimal.valueOf(13.55), LocalDateTime.now());
    }

    public static TradePrice createTradePriceByVendorId(String id, String vendorId) {
        return new TradePrice(id, new Vendor(Long.valueOf(vendorId), "TEST_Vendor"),
                new Instrument(Long.valueOf(11), "instrumentName"),
                BigDecimal.valueOf(11.34), BigDecimal.valueOf(13.55), LocalDateTime.now());
    }
}
