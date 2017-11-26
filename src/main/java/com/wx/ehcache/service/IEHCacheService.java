package com.wx.ehcache.service;

import com.wx.ehcache.model.CacheVO;

import java.util.List;

public interface IEHCacheService {
    String[] getCacheNames();

    void add(String cacheName, String key, String value);

    void remove(String cacheName, String key);

    String get(String cacheName, String key);

    void removeAll(String cacheName);

    List<CacheVO> listAll(String cacheName);
}
