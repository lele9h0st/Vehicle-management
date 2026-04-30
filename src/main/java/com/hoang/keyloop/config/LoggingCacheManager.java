package com.hoang.keyloop.config;

import java.util.Collection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * A {@link CacheManager} decorator that wraps every returned {@link Cache}
 * in a {@link LoggingCache} so that cache hits and misses are logged.
 */
public class LoggingCacheManager implements CacheManager {

    private final CacheManager delegate;

    public LoggingCacheManager(CacheManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = delegate.getCache(name);
        return cache != null ? new LoggingCache(cache) : null;
    }

    @Override
    public Collection<String> getCacheNames() {
        return delegate.getCacheNames();
    }
}
