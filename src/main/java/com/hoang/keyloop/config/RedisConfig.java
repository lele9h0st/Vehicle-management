package com.hoang.keyloop.config;
 
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis cache configuration.
 *
 * <ul>
 * <li>TTL: 5 minutes ± a random offset of −10 to +10 seconds (jitter computed
 * at startup).
 * Jitter prevents simultaneous cache stampedes when multiple instances restart
 * at the same time.
 * <li>Values are serialized as JSON via Jackson so they are human-readable in
 * Redis.
 * <li>Every cache returned by the manager is wrapped in {@link LoggingCache}
 * for HIT/MISS logging.
 * </ul>
 */
@Configuration
@EnableCaching
public class RedisConfig {

        private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

        private static final long BASE_TTL_SECONDS = 300L; // 5 minutes
        private static final long JITTER_RANGE_SECONDS = 10L;

        @Bean
        public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
                long jitter = ThreadLocalRandom.current().nextLong(-JITTER_RANGE_SECONDS, JITTER_RANGE_SECONDS + 1);
                Duration ttl = Duration.ofSeconds(BASE_TTL_SECONDS + jitter);
                logger.info("Cache TTL configured: {}s (base={}s, jitter={}s)", ttl.getSeconds(), BASE_TTL_SECONDS,
                                jitter);

                RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(ttl)
                                .disableCachingNullValues()
                                .serializeKeysWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(RedisSerializer.json()));

                RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(cacheConfig)
                                .build();

                return new LoggingCacheManager(redisCacheManager);
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(connectionFactory);
                template.setKeySerializer(new StringRedisSerializer());
                template.setValueSerializer(RedisSerializer.json());
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(RedisSerializer.json());
                template.afterPropertiesSet();
                return template;
        }
}
