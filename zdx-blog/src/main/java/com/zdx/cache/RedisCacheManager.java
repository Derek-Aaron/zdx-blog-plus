package com.zdx.cache;

import cn.hutool.core.util.ObjUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisCacheManager<K, V> implements CacheTemplate<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    public RedisCacheManager(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean put(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    @Override
    public Object getNativeCache() {
        Set<K> keys = redisTemplate.keys((K) "*");
        Map<K, V> map = new HashMap<>();
        if (ObjUtil.isNotNull(keys) && !keys.isEmpty()) {
            for (K key : keys) {
                map.put(key,redisTemplate.opsForValue().get(key));
            }
        }
        return map;
    }

    @Override
    public boolean put(K key, V value, Long time) {
        redisTemplate.opsForValue().set(key, value, time);
        return true;
    }

    @Override
    public boolean put(K key, V value, Long time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time, unit);
        return true;
    }

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<V> getInclude(K key) {
        if (ObjUtil.isNull(key)) {
            return new ArrayList<>();
        }
        Set<K> keys = redisTemplate.keys(key);
        List<V> objs = new ArrayList<>();
        if (ObjUtil.isNotNull(keys) && !keys.isEmpty()) {
            for (K s : keys) {
                objs.add(redisTemplate.opsForValue().get(s));
            }
        }
        return objs;
    }

    @Override
    public void refreshTime(K key, Long time) {
        redisTemplate.expire(key, Duration.ofMillis(time));
    }

    @Override
    public void refreshTime(K key, Long time, TimeUnit unit) {
        redisTemplate.expire(key, time, unit);
    }

    @Override
    public boolean checkExpiry(K key) {
        Long expire = redisTemplate.getExpire(key);
        return expire != null && expire > 0;
    }

    @Override
    public boolean checkExpiry(K key, boolean isRemove) {
        if (checkExpiry(key)) {
            if (isRemove) {
                redisTemplate.delete(key);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(K key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public boolean hasKey(K key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
