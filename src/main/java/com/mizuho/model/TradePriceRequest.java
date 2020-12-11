package com.mizuho.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TradePriceRequest {
    private Long vendorId;
    private Long instrumentId;
    private String name;
    private String instrumentName;
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDateTime timestamp;

    public TradePriceRequest() {

    }

    public TradePriceRequest(Long vendorId, Long instrumentId, String name, String instrumentName,
                             BigDecimal bid, BigDecimal ask, LocalDateTime timestamp) {
        this.vendorId = vendorId;
        this.instrumentId = instrumentId;
        this.name = name;
        this.instrumentName = instrumentName;
        this.bid = bid;
        this.ask = ask;
        this.timestamp = timestamp;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public String getName() {
        return name;
    }

    public String getInstrumentName() {
        return instrumentName;
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
        return "TradePriceRequest [" +
                "vendorId=" + vendorId +
                ", instrumentId=" + instrumentId +
                ", name=" + name +
                ", instrumentName=" + instrumentName +
                ", bid=" + bid +
                ", ask=" + ask +
                ", timestamp=" + timestamp + ']';
    }

}
