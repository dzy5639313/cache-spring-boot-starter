package com.github.dzy5639313.cache;

import java.util.Map;

/**
 * @author solomon
 * @date 2021-04-12 10:54:51
 */
public class Cache<K, V> {
    private String cacheName;
    Cache(String cacheName){
        this.cacheName = cacheName;
    }
    /**
     * 获取缓存
     * @param k
     * @author solomon
     * @date 2021-04-12 10:58:30
     * @return V
     */
    public V get(K k){
        return CacheService.get(cacheName, k);
    }

    public V getIfPresent(K k){
        return CacheService.getIfPresent(cacheName, k);
    }

    public void put(K k, V v){
        CacheService.put(cacheName, k, v);
    }

    public void putAll(Map<K, V> map){
        CacheService.putAll(cacheName, map);
    }
    /**
     * 清空缓存
     * @param k
     * @author solomon
     * @date 2021-04-12 10:58:40
     * @return void
     */
    public void invalidate(K k){
        CacheService.invalidate(cacheName, k);
    }
    /**
     * 清空所有缓存
     * @author solomon
     * @date 2021-04-12 10:58:47
     * @return void
     */
    public void invalidateAll(){
        CacheService.invalidate(cacheName, null);
    }
    /**
     * 清空所有缓存
     * @author solomon
     * @date 2021-04-12 10:58:47
     * @return void
     */
    public void invalidateAll(Iterable<?> keys){
        CacheService.invalidate(cacheName, keys);
    }
}
