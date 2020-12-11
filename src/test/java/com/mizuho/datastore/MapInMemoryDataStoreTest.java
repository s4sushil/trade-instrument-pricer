package com.mizuho.datastore;

import com.mizuho.model.TradePrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.mizuho.testutils.TradePriceStub.createTradePrice;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MapInMemoryDataStoreTest {

    private MapInMemoryDataStore<String, TradePrice> inMemoryDataStore;

    @BeforeEach
    public void init() {
        inMemoryDataStore = new MapInMemoryDataStore();
    }

    @Test
    public void testLoadAllElements() {
        inMemoryDataStore.addOrUpdate("test1", createTradePrice("Id1"));
        inMemoryDataStore.addOrUpdate("test2", createTradePrice("Id2"));
        Set<TradePrice> prices = inMemoryDataStore.loadAll();
        assertThat(prices.size(), is(2));
    }

    @Test
    public void testUniqueElements() {
        inMemoryDataStore.addOrUpdate("test1", createTradePrice("Id1"));
        inMemoryDataStore.addOrUpdate("test1", createTradePrice("Id1"));
        Set<TradePrice> prices = inMemoryDataStore.loadAll();
        assertThat(prices.size(), is(1));
    }

}