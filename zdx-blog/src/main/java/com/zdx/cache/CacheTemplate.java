package com.zdx.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CacheTemplate<K, V> {
    /**
     * 添加缓存
     * @param key
     * @param value
     */
    boolean put(K key, V value);

    Object getNativeCache();

    /**
     * 添加缓存并存有时间
     * @param key
     * @param value
     * @param time
     * @return
     */
    boolean put(K key, V value, Long time);

    /**
     * 添加缓存加入时间单位
     * @param key
     * @param value
     * @param time
     * @param unit
     * @return
     */
    boolean put(K key, V value, Long time, TimeUnit unit);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    V  get(K key);


    /**
     * 包含查询key
     * @param key
     * @return
     */
    List<V> getInclude(K key);

    /**
     * 刷新缓存时间
     * @param key
     * @param time
     */
    void refreshTime(K key, Long time);

    /**
     * 刷新缓存时间加入单位
     * @param key
     * @param time
     * @param unit
     */
    void refreshTime(K key, Long time, TimeUnit unit);


    /**
     * 检验缓存是否有效 本地
     * @param key
     * @return
     */
    boolean checkExpiry(K key);

    /**
     * 检验缓存是否有效并删除
     * @param key
     * @param isRemove
     * @return
     */
    boolean checkExpiry(K key, boolean isRemove);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean remove(K key);

    /**
     * 是否包含某个key
     * @param key
     * @return
     */
    boolean hasKey(K key);

}
