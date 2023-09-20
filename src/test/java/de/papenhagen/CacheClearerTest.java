package de.papenhagen;


import io.quarkus.cache.CacheManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CacheClearerTest {

    @Mock
    private CacheManager cacheManager;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void statUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache(anyString())).thenReturn(Optional.empty());
    }

    @AfterEach
    void cleanUp() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void clearCache() {
        CacheClearer cacheClearer = new CacheClearer(cacheManager);
        final String cacheName = "abc";
        cacheClearer.clearCache(cacheName);

        verify(cacheManager, times(1)).getCache(eq(cacheName));
    }
}
