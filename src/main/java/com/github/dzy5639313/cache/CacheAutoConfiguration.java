package com.github.dzy5639313.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * @author solomon
 * @date 2021-04-12 10:04:38
 */
@Slf4j
@Configuration
@RemoteApplicationEventScan(basePackageClasses = CacheInvalidateEvent.class)
@AutoConfigureBefore(AutoConfigurationPackage.class)
public class CacheAutoConfiguration {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private BusProperties busProperties;
    @Value("${spring.application.name:application}")
    private String serverName;
    @Autowired
    private ServiceMatcher serviceMatcher;
    @Autowired
    private CacheService cacheService;

    @EventListener
    public void onCacheRefreshEvent(CacheInvalidateEvent event) {
        boolean needProcess = !serviceMatcher.isFromSelf(event) && serviceMatcher.isForSelf(event);
        log.info("收到缓存更新事件, event: {}, needProcess: {}", event, needProcess);
        if(needProcess) {
            cacheService.invalidateCache(event);
        }
    }
    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService(){
        return new CacheService(eventPublisher, busProperties, serverName);
    }
}
