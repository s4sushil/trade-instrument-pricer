package com.mizuho.service;

import com.mizuho.model.TradePrice;

import java.util.Collection;

public interface TradeInstrumentPriceService {

    void addOrUpdate(TradePrice tradePrice);
    Collection<TradePrice> findByVendor(Long vendorId);
    Collection<TradePrice> findByInstrument(String instrumentName);
    Collection<TradePrice> loadAll();

}
