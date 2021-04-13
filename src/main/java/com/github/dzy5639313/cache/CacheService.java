package com.github.dzy5639313.cache;

import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存服务
 * @author solomon
 * @date 2021-04-12 10:39:56
 */
@Slf4j
public class CacheService {
    private static ApplicationEventPublisher eventPublisher;
    private static BusProperties busProperties;
    private static String serverName;

    public CacheService(ApplicationEventPublisher eventPublisher, BusProperties busProperties, String serverName) {
        CacheService.eventPublisher = eventPublisher;
        CacheService.busProperties = busProperties;
        CacheService.serverName = serverName;
    }

    private static Map<String, LoadingCache<Object, Object>> cacheMap = new ConcurrentHashMap<>();

    /**
     * 注册缓存
     * @param cacheName 缓存名称
     * @param cache 缓存
     * @author solomon
     * @date 2021-04-12 13:43:52
     * @return void
     */
    static void registerCache(String cacheName, LoadingCache cache){
        cacheMap.put(cacheName, cache);
    }
    static void put(String cacheName, Object cacheKey, Object cacheValue){
        LoadingCache<Object, Object> cache = cacheMap.get(cacheName);
        if(cache != null){
            cache.put(cacheKey, cacheValue);
        }
    }
    static void putAll(String cacheName, Map map){
        LoadingCache<Object, Object> cache = cacheMap.get(cacheName);
        if(cache != null){
            cache.putAll(map);
        }
    }

    static <T> T get(String cacheName, Object cacheKey){
        LoadingCache<Object, Object> cache = cacheMap.get(cacheName);
        if(cache == null){
            return null;
        }
        return (T) cache.get(cacheKey);
    }

    static <T> T getIfPresent(String cacheName, Object cacheKey){
        LoadingCache<Object, Object> cache = cacheMap.get(cacheName);
        if(cache == null){
            return null;
        }
        return (T) cache.getIfPresent(cacheKey);
    }

    static void invalidate(String cacheName, Object cacheKey){
        // 清理自身缓存
        invalid(cacheName, cacheKey);
        // 发布清理缓存事件
        publish(cacheName, cacheKey);
    }
    private static void invalid(String cacheName, Object cacheKey){
        LoadingCache<Object, Object> cache = cacheMap.get(cacheName);
        if(cache != null){
            if(cacheKey == null){
                cache.invalidateAll();
            }else if(cacheKey instanceof Iterable){
                cache.invalidateAll((Iterable<?>) cacheKey);
            }else{
                cache.invalidate(cacheKey);
            }
        }
    }
    private static void publish(String cacheName, Object cacheKey) {
        CacheInvalidateEvent.InvalidateBody body = new CacheInvalidateEvent.InvalidateBody(cacheName, cacheKey);
        CacheInvalidateEvent event = new CacheInvalidateEvent(body, busProperties.getId(), serverName);
        eventPublisher.publishEvent(event);
    }

    public void invalidateCache(CacheInvalidateEvent event){
        CacheInvalidateEvent.InvalidateBody body = event.getBody();
        invalid(body.getCacheName(), body.getCacheKey());
    }
}
