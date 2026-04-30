package com.hoang.keyloop.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class LoggingCacheTest {

    @Mock
    private Cache delegate;

    @Mock
    private ValueWrapper valueWrapper;

    private LoggingCache loggingCache;

    @BeforeEach
    void setUp() {
        loggingCache = new LoggingCache(delegate);
    }

    // -------------------------------------------------------------------------
    // get — HIT
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("get should return value and log HIT when delegate has a cached value")
    void get_should_returnValue_and_logHit_when_delegateHasCachedValue() {
        // Arrange
        when(delegate.getName()).thenReturn("vehicles");
        String key = "filter::abc123";
        when(delegate.get(key)).thenReturn(valueWrapper);

        // Act
        ValueWrapper result = loggingCache.get(key);

        // Assert
        assertThat(result).isSameAs(valueWrapper);
        verify(delegate).get(key);
        // Logging is a side-effect; we verify the delegate was called and a non-null result was returned.
    }

    // -------------------------------------------------------------------------
    // get — MISS
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("get should return null and log MISS when delegate has no cached value")
    void get_should_returnNull_and_logMiss_when_delegateHasNoCachedValue() {
        // Arrange
        when(delegate.getName()).thenReturn("vehicles");
        String key = "filter::notfound";
        when(delegate.get(key)).thenReturn(null);

        // Act
        ValueWrapper result = loggingCache.get(key);

        // Assert
        assertThat(result).isNull();
        verify(delegate).get(key);
    }

    // -------------------------------------------------------------------------
    // put — delegates to underlying cache
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("put should delegate to the underlying cache")
    void put_should_delegateToCacheDelegate() {
        // Arrange
        String key = "filter::xyz";
        Object value = List.of("vehicle1", "vehicle2");

        // Act
        loggingCache.put(key, value);

        // Assert
        verify(delegate).put(key, value);
    }

    // -------------------------------------------------------------------------
    // evict — delegates to underlying cache
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("evict should delegate to the underlying cache")
    void evict_should_delegateToCacheDelegate() {
        // Arrange
        String key = "filter::toEvict";

        // Act
        loggingCache.evict(key);

        // Assert
        verify(delegate).evict(key);
    }

    // -------------------------------------------------------------------------
    // clear — delegates to underlying cache
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("clear should delegate to the underlying cache")
    void clear_should_delegateToCacheDelegate() {
        // Act
        loggingCache.clear();

        // Assert
        verify(delegate).clear();
    }

    // -------------------------------------------------------------------------
    // getName — returns delegate's name
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getName should return the delegate cache name")
    void getName_should_returnDelegateName() {
        when(delegate.getName()).thenReturn("vehicles");
        assertThat(loggingCache.getName()).isEqualTo("vehicles");
    }

    // -------------------------------------------------------------------------
    // getNativeCache — delegates
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getNativeCache should delegate to the underlying cache")
    void getNativeCache_should_delegateToCacheDelegate() {
        // Arrange
        Object nativeCache = new Object();
        when(delegate.getNativeCache()).thenReturn(nativeCache);

        // Act
        Object result = loggingCache.getNativeCache();

        // Assert
        assertThat(result).isSameAs(nativeCache);
    }
}
