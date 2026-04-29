package com.hoang.keyloop.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Use IP address or session as key. For now, using a global key since requirement doesn't specify per user.
        // But usually rate limiting is per IP or per user.
        String key = "global"; // Requirement: "only 20 requests per 10 seconds" for ANY request?
        // Let's assume it's per IP to be more realistic, or just global if strictly following.
        // "ratelimiter should be applied for any request"
        
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests");
        }
    }

    private Bucket createNewBucket() {
        // 20 requests per 10 seconds
        return Bucket.builder()
                .addLimit(Bandwidth.classic(20, Refill.greedy(20, Duration.ofSeconds(10))))
                .build();
    }
}
