package com.mizuho.service;

import com.mizuho.datastore.MapInMemoryDataStore;
import com.mizuho.model.TradePrice;
import com.mizuho.publish.TradePricePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TradeInstrumentPriceServiceImpl implements TradeInstrumentPriceService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TradeInstrumentPriceServiceImpl.class);

    private TradePricePublisher tradePricePublisher;

    private CacheManager cacheManager;

    private MapInMemoryDataStore<String, TradePrice> inMemoryDataStore;

    public TradeInstrumentPriceServiceImpl(MapInMemoryDataStore<String, TradePrice> inMemoryDataStore,
                            CacheManager cacheManager, TradePricePublisher tradePricePublisher) {
        this.inMemoryDataStore = inMemoryDataStore;
        this.cacheManager = cacheManager;
        this.tradePricePublisher = tradePricePublisher;
    }

    @Override
    public void addOrUpdate(TradePrice tradePrice) {
        LOGGER.info("Add or Update trade price");
        updateVendorCache(tradePrice);
        updateInstrumentCache(tradePrice);

        inMemoryDataStore.addOrUpdate(tradePrice.getId(), tradePrice);
        tradePricePublisher.publish(tradePrice);
    }

    @Cacheable(cacheNames = "vendorPrices")
    @Override
    public Collection<TradePrice> findByVendor(Long vendorId) {
        LOGGER.info("Find Trade price By Vendor Id");
        List<TradePrice> tradePriceListByVendor = inMemoryDataStore.loadAll()
                .stream()
                .filter(tradePrice -> vendorId.equals(tradePrice.getVendor().getId()))
                .collect(Collectors.toList());
        return new HashSet<>(tradePriceListByVendor);
    }

    @Cacheable(cacheNames = "instrumentPrices")
    @Override
    public Collection<TradePrice> findByInstrument(String instrumentName) {
        LOGGER.info("Find Trade price By Instrument Name");
        List<TradePrice> tradePriceListByInstrument = inMemoryDataStore.loadAll()
                .stream()
                .filter(tradePrice -> instrumentName.equals(tradePrice.getInstrument().getInstrumentName()))
                .collect(Collectors.toList());
        return new HashSet<>(tradePriceListByInstrument);
    }

    @Override
    public Collection<TradePrice> loadAll() {
        return inMemoryDataStore.loadAll();
    }

    private void updateInstrumentCache(TradePrice price) {
        Cache cache = cacheManager.getCache("instrumentPrices");
        Cache.ValueWrapper instrumentCache = cache.get(price.getInstrument().getId());
        HashSet<TradePrice> prices;
        if (instrumentCache != null) {
            prices = ((HashSet) instrumentCache.get());
            prices.add(price);
        } else {
            prices = new HashSet(Arrays.asList(price));
        }

        cache.put(price.getInstrument().getId(), prices);
    }

    private void updateVendorCache(TradePrice price) {
        Cache cache = cacheManager.getCache("vendorPrices");
        Cache.ValueWrapper instrumentCache = cache.get(price.getVendor().getId());
        HashSet<TradePrice> tradePrices;
        if (instrumentCache != null) {
            tradePrices = ((HashSet) instrumentCache.get());
            tradePrices.add(price);
        } else {
            tradePrices = new HashSet(Arrays.asList(price));
        }
        cache.put(price.getVendor().getId(), tradePrices);

    }
}
