package com.wx.ehcache.service;

import com.wx.ehcache.model.CacheVO;
import com.wx.ehcache.model.InfoVO;

import java.util.List;
import java.util.Map;

public interface IEHCacheService {
    String[] getCacheNames();

    void add(String cacheName, String key, Object value);

    void remove(String cacheName, String key);

    Object get(String cacheName, String key);

    void removeAll(String cacheName);

    List<CacheVO> listAll(String cacheName);

    List<InfoVO> getInfo(String cacheName);
}
