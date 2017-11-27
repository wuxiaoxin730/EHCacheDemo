package com.wx.ehcache.service.impl;

import com.wx.ehcache.model.CacheVO;
import com.wx.ehcache.service.IEHCacheService;
import com.wx.ehcache.util.EHCacheUtil;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EHCacheServiceImpl implements IEHCacheService {

    @Override
    public String[] getCacheNames() {
        return EHCacheUtil.retrieveCacheNames();
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
        Element element = EHCacheUtil.getElement(cacheName, key);
        String result = "";
        if (element != null) {
            result = (String) element.getObjectValue();
        }
        return result;
    }

    @Override
    public void removeAll(String cacheName) {
        EHCacheUtil.removeCache(cacheName);
    }

    @Override
    public List<CacheVO> listAll(String cacheName) {
        Map<Object, Element> elements = EHCacheUtil.listAllElements(cacheName);
        List<CacheVO> result = new ArrayList<>();
        if (elements != null) {
            for (Object key : elements.keySet()) {
                CacheVO cacheVO = new CacheVO();
                cacheVO.setKey(key.toString());
                cacheVO.setValue(elements.get(key).getObjectValue().toString());
                cacheVO.setCreateTime(new Date(elements.get(key).getLatestOfCreationAndUpdateTime()));
                result.add(cacheVO);
            }
        }
        return result;
    }
}
