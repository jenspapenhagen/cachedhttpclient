package de.papenhagen;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheManager;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class CacheClearer {
    private final CacheManager cacheManager;

    public CacheClearer(final CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void clearCache(final String cacheName) {
        Optional<Cache> cache = cacheManager.getCache(cacheName);
        cache.ifPresent(c -> {
            c.invalidateAll().await().indefinitely();
        });
    }
}
