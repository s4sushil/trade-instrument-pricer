package com.mizuho.datastore;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapInMemoryDataStore<K, V> implements MemoryDataStore<K, V> {

    private final ConcurrentMap<K, V> inMemoryStore;

    public MapInMemoryDataStore() {
        inMemoryStore = new ConcurrentHashMap<>();
    }

    @Override
    public Set<V> loadAll() {
        return new HashSet<V>(inMemoryStore.values());
    }

    @Override
    public void addOrUpdate(K key, V value) {
        inMemoryStore.putIfAbsent(key, value);
    }
}
