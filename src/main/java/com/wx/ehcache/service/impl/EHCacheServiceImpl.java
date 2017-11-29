package com.wx.ehcache.service.impl;

import com.wx.ehcache.exception.EHCacheException;
import com.wx.ehcache.model.CacheVO;
import com.wx.ehcache.model.InfoVO;
import com.wx.ehcache.service.IEHCacheService;
import com.wx.ehcache.util.EHCacheUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EHCacheServiceImpl implements IEHCacheService {

    @Override
    public String[] getCacheNames() {
        throw new EHCacheException();
    }

    @Override
    public void add(String cacheName, String key, String value) {
        EHCacheUtil.addElement(cacheName, key, value);
    }

    @Override
    public void remove(String cacheName, String key) {
        EHCacheUtil.removeElement(cacheName, key);
    }

    @Override
    public String get(String cacheName, String key) {
        return EHCacheUtil.getElement(cacheName, key);
    }

    @Override
    public void removeAll(String cacheName) {
        EHCacheUtil.removeCache(cacheName);
    }

    @Override
    public List<CacheVO> listAll(String cacheName) {
        List<CacheVO> cacheVOList = new ArrayList<>();
        Map<String, String> map = EHCacheUtil.listAll(cacheName);
        map.forEach((k, v) -> {
            CacheVO cacheVO = new CacheVO();
            cacheVO.setKey(k);
            cacheVO.setValue(v);
            cacheVOList.add(cacheVO);
        });
        return cacheVOList;
    }

    @Override
    public List<InfoVO> getInfo(String cacheName) {
        throw new EHCacheException();
    }
}
