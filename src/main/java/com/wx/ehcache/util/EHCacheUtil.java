package com.wx.ehcache.util;

import org.ehcache.Cache;
import org.ehcache.CachePersistenceException;
import org.ehcache.PersistentCacheManager;
import org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
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
                        .cluster(URI.create("terracotta://localhost/my-application"))
                        .autoCreate())
                .build(true);
    }

    public static void createDefaultCache(String cacheName) {
        persistentCacheManager.createCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(1000)).build());
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
        String value = null;
        Cache<String, String> cache = persistentCacheManager.getCache(cacheName, String.class, String.class);
        value = cache.get(key);
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
