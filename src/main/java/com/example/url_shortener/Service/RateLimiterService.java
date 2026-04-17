package com.example.url_shortener.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private static final int LIMIT = 10;
    private static final int WINDOW = 60; // seconds

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(String ip) {

        String key = "rate_limit:" + ip;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            // first request → set expiry
            redisTemplate.expire(key, WINDOW, TimeUnit.SECONDS);
        }

        return count <= LIMIT;
    }
}
