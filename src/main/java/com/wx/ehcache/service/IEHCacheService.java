package com.wx.ehcache.service;

import com.wx.ehcache.model.CacheVO;
import com.wx.ehcache.model.InfoVO;

import java.util.List;
import java.util.Map;

public interface IEHCacheService {
    @Deprecated
    String[] getCacheNames();

    void add(String cacheName, String key, String value);

    void remove(String cacheName, String key);

    String get(String cacheName, String key);

    void removeAll(String cacheName);

    List<CacheVO> listAll(String cacheName);

    @Deprecated
    List<InfoVO> getInfo(String cacheName);
}
