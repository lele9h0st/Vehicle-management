package com.hoang.keyloop.config;

import com.hoang.keyloop.controller.dto.VehicleFilterRequest;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * Generates a stable, compact cache key from a {@link VehicleFilterRequest}
 * by hashing its string representation with SHA-256.
 *
 * <p>Lombok's {@code @Data} provides a deterministic {@code toString()} that
 * includes every field, so two requests with identical parameters always
 * produce the same hash and therefore the same cache key.
 */
@Component("vehicleFilterCacheKeyGenerator")
public class VehicleFilterCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length == 0 || params[0] == null) {
            return "default";
        }
        VehicleFilterRequest request = (VehicleFilterRequest) params[0];
        return sha256(request.toString());
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available in every JVM
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
