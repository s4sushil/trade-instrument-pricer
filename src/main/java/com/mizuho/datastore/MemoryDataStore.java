package com.mizuho.datastore;

import java.util.Set;

public interface MemoryDataStore<K, V> {

    Set<V> loadAll();
    void addOrUpdate(K key, V value);

}
