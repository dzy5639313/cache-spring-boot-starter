package com.github.dzy5639313.cache;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * 缓存失效事件
 * @author solomon
 * @date 2021-04-12 10:06:37
 */
@Data
public class CacheInvalidateEvent extends RemoteApplicationEvent {
    private InvalidateBody body;
    private CacheInvalidateEvent(){

    }

    public CacheInvalidateEvent(InvalidateBody body, String originService,
                                String destinationService) {
        super(JSONUtil.toJsonStr(body), originService, destinationService);
        this.body = body;
    }
    @Data
    static class InvalidateBody {
        private String cacheName;
        private Object cacheKey;
        public InvalidateBody(String cacheName, Object cacheKey) {
            this.cacheName = cacheName;
            this.cacheKey = cacheKey;
        }
    }
}
