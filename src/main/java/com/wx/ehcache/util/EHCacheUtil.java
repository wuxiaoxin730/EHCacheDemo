package com.wx.ehcache.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.TerracottaConfiguration;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.MulticastRMICacheManagerPeerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EHCacheUtil {
    private static CacheManager cacheManager;

    private static Logger logger = LoggerFactory.getLogger(EHCacheUtil.class);

    static {
        cacheManager = CacheManager.create();
    }

    public static String[] retrieveCacheNames() {
        return cacheManager.getCacheNames();
    }

    public static void createDefaultCache(String cacheName) {
        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfiguration = new CacheConfiguration(cacheName, Constants.DEFAULT_MAX_ENTRIES_LOCAL_HEAP);
            cacheConfiguration.addTerracotta(new TerracottaConfiguration());
            cacheManager.addCache(new Cache(cacheConfiguration));
        } else {
            logger.warn("The cache(" + cacheName + ") already existed");
        }
    }

    public static void removeCache(String cacheName) {
        if (cacheManager.cacheExists(cacheName)) {
            cacheManager.removeCache(cacheName);
        } else {
            logger.warn("The cache(" + cacheName + ") does not exist");
        }
    }

    public static void addElement(String cacheName, Element element) {
        if (!cacheManager.cacheExists(cacheName)) {
            createDefaultCache(cacheName);
        }
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(element);
    }

    public static void addElement(String cacheName, Object elementKey, Object elementValue) {
        Element element = new Element(elementKey, elementValue);
        addElement(cacheName, element);
    }

    public static Map<Object, Element> listAllElements(String cacheName) {
        getCacheManagerInfo();
        Map<Object, Element> result = null;
        if (cacheManager.cacheExists(cacheName)) {
            Cache cache = cacheManager.getCache(cacheName);
            result = cache.getAll(cache.getKeys());
            result.putAll(cache.getAll(cache.getKeysWithExpiryCheck()));
            result.putAll(cache.getAll(cache.getKeysNoDuplicateCheck()));
        } else {
            result = new HashMap<>();
            logger.warn("No cache(" + cacheName + ") exist");
        }
        return result;
    }

    public static Element getElement(String cacheName, Object key) {
        Element element = null;
        if (cacheManager.cacheExists(cacheName)) {
            element = cacheManager.getCache(cacheName).get(key);
        } else {
            logger.warn("No element found in cache(" + cacheName + ")");
        }
        return element;
    }

    public static void removeElement(String cacheName, Object key) {
        if (cacheManager.cacheExists(cacheName)) {
            cacheManager.getCache(cacheName).remove(key);
        } else {
            logger.warn("No element found in cache(" + cacheName + ")");
        }
    }

    public static Map<String, String> getCacheManagerInfo() {
        Map<String, String> result = new HashMap<>();
        Map<String, CacheManagerPeerProvider> cacheManagerPeerProviderMap = cacheManager.getCacheManagerPeerProviders();
        if (cacheManagerPeerProviderMap != null) {
            for (String name : cacheManagerPeerProviderMap.keySet()) {
                result.put("Peer Provider Name", name);
                CacheManagerPeerProvider cacheManagerPeerProvider = cacheManagerPeerProviderMap.get(name);
                result.put("Is RMI Cache Manager", String.valueOf((cacheManagerPeerProvider instanceof MulticastRMICacheManagerPeerProvider)));
                result.put("Scheme", cacheManagerPeerProvider.getScheme());
                cacheManagerPeerProvider.init();
                List remoteCachePeers = cacheManagerPeerProvider.listRemoteCachePeers(cacheManager.getEhcache("ClusteringCache"));
                if (remoteCachePeers != null) {
                    for (int i = 0; i < remoteCachePeers.size(); i++) {
                        if (remoteCachePeers.get(i) instanceof CachePeer) {
                            CachePeer cachePeer = (CachePeer) remoteCachePeers.get(i);
                            try {
                                result.put("Peer Name", cachePeer.getName());
                                result.put("Peer Url", cachePeer.getUrl());
                                result.put("Peer Guid", cachePeer.getGuid());
                                result.put("Peer Url Base", cachePeer.getUrlBase());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void destroy() {
        if (cacheManager != null) {
            cacheManager.shutdown();
        } else {
            logger.warn("The cache manager has already been shut down!");
        }
    }
}
