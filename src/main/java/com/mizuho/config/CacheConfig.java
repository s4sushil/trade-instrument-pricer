package com.mizuho.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashSet;

import static java.time.temporal.ChronoUnit.DAYS;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache-max-entries:10000}")
    private int cacheMaxEntries;

    @Value("${cache-time-to-live-days:30}")
    private int cacheTimeToLiveDays;

    @Bean
    public JCacheManagerCustomizer cacheCustomizer() {
        javax.cache.configuration.Configuration<Object, HashSet> cacheConfiguration = Eh107Configuration
                .fromEhcacheCacheConfiguration(CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(Object.class, HashSet.class, ResourcePoolsBuilder.heap(cacheMaxEntries))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.of(cacheTimeToLiveDays, DAYS)))
                        .build());
        return cfg -> {
            cfg.createCache("vendorPrices", cacheConfiguration);
            cfg.createCache("instrumentPrices", cacheConfiguration);
        };
    }
}

