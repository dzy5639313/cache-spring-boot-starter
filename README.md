# cache-spring-boot-starter

## 介绍
基于spring cloud bus + rabbitmq + caffeine, 可以自动更新集群内同服务所有实例的内存缓存
## 使用说明

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.github.dzy5639313</groupId>
    <artifactId>cache-spring-boot-starter</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

### 2. 配置参数

```yaml
spring:
  rabbitmq:
    password: 
    port: 
    host: 
    username: 
```

### 3. 代码中使用

```java
// 和Caffeine的使用方式相同
private Cache<String, String> cache = CacheBuilder.newBuilder("test")
            .softValues()
            .maximumSize(10)
            .expireAfterWrite(Duration.ofSeconds(60))
            .build(key -> {
                log.info("开始构建缓存==================== "+key);
                return key;
            });

@GetMapping("get")
public String get(String key){
    return cache.get(key);
}
@GetMapping("invalidate")
public boolean invalidate(String key){
    cache.invalidate(key);
    return true;
}

```
注意: 相同类型的缓存请在使用CacheBuilder.newBuilder(String cacheName)时传入相同的cacheName