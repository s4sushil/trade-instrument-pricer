package com.mizuho.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class TradePrice implements Data {

    private final String id;
    private final Vendor vendor;
    private final Instrument instrument;
    private final BigDecimal bid;
    private final BigDecimal ask;
    private final LocalDateTime timestamp;

    public TradePrice(String id, Vendor vendor, Instrument instrument, BigDecimal bid,
                      BigDecimal ask, LocalDateTime timestamp) {
        this.id = id;
        this.vendor = vendor;
        this.instrument = instrument;
        this.bid = bid;
        this.ask = ask;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TradePrice [" +
                "id=" + id +
                "vendor=" + vendor +
                ", instrument=" + instrument +
                ", bid=" + bid +
                ", ask=" + ask +
                ", timestamp=" + timestamp + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vendor, instrument);
    }

    @Override
    public boolean equals(Data obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TradePrice)) {
            return false;
        }
        TradePrice that = (TradePrice) obj;
        return Objects.equals(vendor, that.vendor)
                && Objects.equals(instrument, that.instrument)
                && Objects.equals(id, that.id);
    }
}
