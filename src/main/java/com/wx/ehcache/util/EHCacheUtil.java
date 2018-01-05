package com.wx.ehcache.util;

import org.ehcache.Cache;
import org.ehcache.CachePersistenceException;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class EHCacheUtil {
    private static PersistentCacheManager persistentCacheManager;

    private static Logger logger = LoggerFactory.getLogger(EHCacheUtil.class);

    static {
        persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(ClusteringServiceConfigurationBuilder
                        .cluster(URI.create("terracotta://localhost/mams"))
                        .autoCreate()
                        .defaultServerResource("primary-server-resource")
                        .resourcePool("resource-poll-a", 96, MemoryUnit.MB, "second-server-resource")
                        .resourcePool("resource-poll-b", 32, MemoryUnit.MB))
                .withCache("clustered-cache-a", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder
                                .newResourcePoolsBuilder()
                                .heap(100)
                                .with(ClusteredResourcePoolBuilder.clusteredDedicated("primary-server-resource", 100, MemoryUnit.MB))))
                .withCache("shared-cache-a", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder
                                .newResourcePoolsBuilder()
                                .with(ClusteredResourcePoolBuilder.clusteredShared("resource-poll-a"))))
                .withCache("shared-cache-b", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder
                                .newResourcePoolsBuilder()
                                .with(ClusteredResourcePoolBuilder.clusteredShared("resource-poll-b"))))
                .build(true);
    }

    public static void createDefaultCache(String cacheName) {
        persistentCacheManager.createCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                ResourcePoolsBuilder
                        .newResourcePoolsBuilder()
                        .heap(100)
                        .with(ClusteredResourcePoolBuilder.clusteredDedicated("resource-poll-b", 32, MemoryUnit.MB)))
                .build());
    }

    public static void removeCache(String cacheName) {
        persistentCacheManager.removeCache(cacheName);
    }

    public static void addElement(String cacheName, String key, String value) {
        Cache<String, String> cache = persistentCacheManager.getCache(cacheName, String.class, String.class);
        if (cache == null) {
            createDefaultCache(cacheName);
            cache = persistentCacheManager.getCache(cacheName, String.class, String.class);
        }
        if (cache.containsKey(key)) {
            cache.replace(key, value);
        } else {
            cache.put(key, value);
        }
    }

    public static String getElement(String cacheName, String key) {
        Cache<String, String> cache = persistentCacheManager.getCache(cacheName, String.class, String.class);
        String value = cache.get(key);
        return value;
    }

    public static void removeElement(String cacheName, String key) {
        Cache<String, String> cache = persistentCacheManager.getCache(cacheName, String.class, String.class);
        cache.remove(key);
    }

    public static Map<String, String> listAll(String cacheName) {
        Cache<String, String> cache = persistentCacheManager.getCache(cacheName, String.class, String.class);
        Map<String, String> map = new HashMap<>();
        cache.forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return map;
    }

    public static void destroy() {
        if (persistentCacheManager != null) {
            try {
                persistentCacheManager.destroy();
            } catch (CachePersistenceException e) {
                logger.error("An error occurs when destroying cache manager!", e);
            }
        } else {
            logger.warn("The cache manager has already been shut down!");
        }
    }
}
