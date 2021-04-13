package com.github.dzy5639313.cache;

import com.github.benmanes.caffeine.cache.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author solomon
 * @date 2021-04-12 11:03:20
 */
public class CacheBuilder<K, V> {
    /**
     * 缓存名称, 同一类型的缓存集合
     * @author solomon
     * @date 2021-04-12 13:36:20
     * @return
     */
    @Setter
    private String cacheName;
    @Setter
    @Getter
    private Caffeine<K, V> builder;

    private CacheBuilder(){}

    public static CacheBuilder<Object, Object> newBuilder(String cacheName){
        CacheBuilder<Object, Object> cacheBuilder = new CacheBuilder<>();
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        cacheBuilder.setBuilder(builder);
        cacheBuilder.setCacheName(cacheName);
        return cacheBuilder;
    }
    public CacheBuilder<K, V> initialCapacity(int initialCapacity){
        getBuilder().initialCapacity(initialCapacity);
        return this;
    }
    public CacheBuilder<K, V> executor(Executor executor){
        getBuilder().executor(executor);
        return this;
    }
    public CacheBuilder<K, V> maximumSize(long maximumSize){
        getBuilder().maximumSize(maximumSize);
        return this;
    }
    public CacheBuilder<K, V> maximumWeight(long maximumWeight){
        getBuilder().maximumWeight(maximumWeight);
        return this;
    }
    public CacheBuilder<K, V> weakKeys(){
        getBuilder().weakKeys();
        return this;
    }
    public CacheBuilder<K, V> weakValues(){
        getBuilder().weakValues();
        return this;
    }
    public CacheBuilder<K, V> softValues(){
        getBuilder().softValues();
        return this;
    }
    public CacheBuilder<K, V> expireAfterWrite(Duration duration){
        return expireAfterWrite(duration.toNanos(), TimeUnit.NANOSECONDS);
    }
    public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit){
        getBuilder().expireAfterWrite(duration, unit);
        return this;
    }
    public CacheBuilder<K, V> expireAfterAccess(Duration duration){
        return expireAfterAccess(duration.toNanos(), TimeUnit.NANOSECONDS);
    }
    public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit){
        getBuilder().expireAfterAccess(duration, unit);
        return this;
    }
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> expireAfter(Expiry<? super K1, ? super V1> expiry){
        getBuilder().expireAfter(expiry);
        return (CacheBuilder<K1, V1>) this;
    }
    public CacheBuilder<K, V> refreshAfterWrite(Duration duration){
        return refreshAfterWrite(duration.toNanos(), TimeUnit.NANOSECONDS);
    }
    public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit){
        getBuilder().refreshAfterWrite(duration, unit);
        return this;
    }
    public CacheBuilder<K, V> ticker(Ticker ticker){
        getBuilder().ticker(ticker);
        return this;
    }
    public CacheBuilder<K, V> removalListener(RemovalListener<K, V> removalListener){
        getBuilder().removalListener(removalListener);
        return this;
    }
    public CacheBuilder<K, V> writer(CacheWriter<K, V> writer){
        getBuilder().writer(writer);
        return this;
    }
    public CacheBuilder<K, V> recordStats(){
        getBuilder().recordStats();
        return this;
    }
    public <K1 extends K, V1 extends V> com.github.dzy5639313.cache.Cache<K1, V1> build(CacheLoader<K1, V1> loader){
        LoadingCache<K1,V1> loadingCache = getBuilder().build(loader);
        com.github.dzy5639313.cache.Cache<K1, V1> cache = new Cache<>(cacheName);
        CacheService.registerCache(cacheName, loadingCache);
        return cache;
    }
}
