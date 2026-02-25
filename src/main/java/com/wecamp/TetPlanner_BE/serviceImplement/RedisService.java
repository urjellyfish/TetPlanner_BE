package com.wecamp.TetPlanner_BE.serviceImplement;

import com.wecamp.TetPlanner_BE.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService implements IRedisService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(String key, String value, long expirationTime) {
        redisTemplate.opsForValue()
                .set(key, value, expirationTime, TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {

        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
