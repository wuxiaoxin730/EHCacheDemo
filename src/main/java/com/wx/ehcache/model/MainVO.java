package com.wx.ehcache.model;

import java.util.List;

public class MainVO {
    private String currentCacheName;

    private String cacheKey;

    private String cacheValue;

    private List<CacheVO> elements;

    public String getCurrentCacheName() {
        return currentCacheName;
    }

    public void setCurrentCacheName(String currentCacheName) {
        this.currentCacheName = currentCacheName;
    }

    public List<CacheVO> getElements() {
        return elements;
    }

    public void setElements(List<CacheVO> elements) {
        this.elements = elements;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }
}
