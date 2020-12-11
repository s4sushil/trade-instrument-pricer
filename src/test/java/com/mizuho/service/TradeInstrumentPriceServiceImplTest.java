package com.mizuho.service;

import com.mizuho.Application;
import com.mizuho.model.TradePrice;
import com.mizuho.testutils.TradePriceStub;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = Application.class)
public class TradeInstrumentPriceServiceImplTest {

    @Autowired
    private TradeInstrumentPriceService tradeInstrumentPriceService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testLoadAllOnAppStartUp() {
        Collection<TradePrice> pricesLoadedOnAppStartUp = tradeInstrumentPriceService.loadAll();
        assertThat(pricesLoadedOnAppStartUp, is(not(empty())));
        assertThat(pricesLoadedOnAppStartUp.size(), is(greaterThan(3)));
    }

    @Test
    public void testFinderByVendorIdAndRetrievedFromVendorKeyCache() {
        TradePrice tradePrice = TradePriceStub.createTradePriceByVendorId("Id9", "6");
        tradeInstrumentPriceService.addOrUpdate(tradePrice);
        Collection<TradePrice> pricesByVendor = tradeInstrumentPriceService.findByVendor(6l);
        assertFalse(pricesByVendor.isEmpty());
        assertThat(pricesByVendor.size(), is(1));
        assertNotNull(cacheManager.getCache("vendorPrices").get(tradePrice.getVendor().getId()).get());

    }

    @Test
    public void testFinderByInValidVendorIdResultsEmpty() {
        TradePrice tradePrice = TradePriceStub.createTradePrice("Id1");
        tradeInstrumentPriceService.addOrUpdate(tradePrice);
        Collection<TradePrice> pricesByVendor = tradeInstrumentPriceService.findByVendor(66l);
        assertTrue(pricesByVendor.isEmpty());
    }

    @Test
    public void testFinderByInstrumentIdAndRetrievedFromInstrumentKeyCache() {
        TradePrice tradePrice = TradePriceStub.createTradePriceByInstrument("Id6", "Spotify");
        tradeInstrumentPriceService.addOrUpdate(tradePrice);
        Collection<TradePrice> pricesByInstrument = tradeInstrumentPriceService.findByInstrument("Spotify");
        assertFalse(pricesByInstrument.isEmpty());
        assertThat(pricesByInstrument.size(), is(1));

        assertNotNull(cacheManager.getCache("instrumentPrices").get(tradePrice.getInstrument().getId()).get());

    }

    @Test
    public void testFinderByInValidInstrumentIdResultsEmpty() {
        TradePrice tradePrice = TradePriceStub.createTradePrice("Id1");
        tradeInstrumentPriceService.addOrUpdate(tradePrice);
        Collection<TradePrice> pricesByInstrument = tradeInstrumentPriceService.findByInstrument("MS-Unknown");
        assertTrue(pricesByInstrument.isEmpty());

    }

}