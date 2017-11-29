package com.wx.ehcache.controller;

import com.wx.ehcache.model.MainVO;
import com.wx.ehcache.service.IEHCacheService;
import com.wx.ehcache.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class EHCacheController {
    @Autowired
    private IEHCacheService cacheService;

    private String currentCacheName;

    private String[] cacheNames;

    @RequestMapping("/")
    public String welcome(@ModelAttribute("currentCacheName") String currentCacheName, @ModelAttribute("mainVO") MainVO mainVO) {
        if (StringUtils.isEmpty(currentCacheName)) {
            this.currentCacheName = Constants.DEFAULT_CACHE_NAME;
            mainVO.setCurrentCacheName(this.currentCacheName);
        }
        return "main";
    }

    @Deprecated
    @RequestMapping(value = "/init")
    public String init(@ModelAttribute("mainVO") MainVO mainVO, @RequestParam String currentCacheName) {
        this.currentCacheName = currentCacheName;
        prepareModelData(mainVO, this.currentCacheName);
        return "main";
    }

    @RequestMapping(value = "/add")
    public String add(@ModelAttribute("mainVO") MainVO mainVO) {
        cacheService.add(this.currentCacheName, mainVO.getCacheKey(), mainVO.getCacheValue());
        prepareModelData(mainVO, this.currentCacheName);
        return "main";
    }

    @RequestMapping(value = "/clear")
    public String clearUp(@ModelAttribute("mainVO") MainVO mainVO) {
        cacheService.removeAll(this.currentCacheName);
        prepareModelData(mainVO, this.currentCacheName);
        return "main";
    }

    @RequestMapping(value = "/search")
    public String search(@ModelAttribute("mainVO") MainVO mainVO) {
        prepareModelData(mainVO, this.currentCacheName);
        return "main";
    }

    @RequestMapping(value = "/delete")
    public String delete(@ModelAttribute("mainVO") MainVO mainVO, @RequestParam String cacheKey) {
        cacheService.remove(this.currentCacheName, cacheKey);
        prepareModelData(mainVO, this.currentCacheName);
        return "main";
    }

    private void prepareModelData(MainVO mainVO, String currentCacheName) {
        mainVO.setCurrentCacheName(currentCacheName);
        mainVO.setElements(cacheService.listAll(currentCacheName));
    }
}
