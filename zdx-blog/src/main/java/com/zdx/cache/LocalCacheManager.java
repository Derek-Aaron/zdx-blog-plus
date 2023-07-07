package com.zdx.cache;

import org.springframework.cache.Cache;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class LocalCacheManager<K, V> implements CacheTemplate<K, V> {


    @Override
    public boolean put(K key, V value) {
        return false;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public boolean put(K key, V value, Long time) {
        return false;
    }

    @Override
    public boolean put(K key, V value, Long time, TimeUnit unit) {
        return false;
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public List<V> getInclude(K key) {
        return null;
    }

    @Override
    public void refreshTime(K key, Long time) {

    }

    @Override
    public void refreshTime(K key, Long time, TimeUnit unit) {

    }

    @Override
    public boolean checkExpiry(K key) {
        return false;
    }

    @Override
    public boolean checkExpiry(K key, boolean isRemove) {
        return false;
    }

    @Override
    public boolean remove(K key) {
        return false;
    }

    @Override
    public boolean hasKey(K key) {
        return false;
    }
}
