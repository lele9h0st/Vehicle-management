package com.hoang.keyloop.config;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * A {@link Cache} decorator that logs HIT / MISS for every {@code get} call,
 * and delegates all other operations transparently to the underlying cache.
 */
public class LoggingCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(LoggingCache.class);

    private final Cache delegate;

    public LoggingCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public Object getNativeCache() {
        return delegate.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper result = delegate.get(key);
        if (result != null) {
            logger.info("Cache HIT  [{}] key={}", delegate.getName(), key);
        } else {
            logger.info("Cache MISS [{}] key={}", delegate.getName(), key);
        }
        return result;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return delegate.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return delegate.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        delegate.put(key, value);
    }

    @Override
    public void evict(Object key) {
        delegate.evict(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        return delegate.evictIfPresent(key);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean invalidate() {
        return delegate.invalidate();
    }
}
