package com.wx.ehcache.controller;

import com.wx.ehcache.model.InfoVO;
import com.wx.ehcache.model.MainVO;
import com.wx.ehcache.service.IEHCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Controller
public class EHCacheController {
    @Autowired
    private IEHCacheService cacheService;

    private String currentCacheName;

    private String[] cacheNames;

    @RequestMapping("/")
    public String welcome(@ModelAttribute("cacheNames") String[] cacheNames, @ModelAttribute("mainVO") MainVO mainVO) {
        if (cacheNames != null && cacheNames.length > 0) {
            this.currentCacheName = cacheNames[0];
            mainVO.setCurrentCacheName(this.currentCacheName);
        }
        return "main";
    }

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

    @ModelAttribute("cacheNames")
    public String[] listAllCacheNames() {
        if (cacheNames == null || cacheNames.length == 0) {
            cacheNames = cacheService.getCacheNames();
        }
        return cacheNames;
    }

    @ModelAttribute("infoList")
    public List<InfoVO> getInfo() {
        return cacheService.getInfo(this.currentCacheName);
    }

    private void prepareModelData(MainVO mainVO, String currentCacheName) {
        mainVO.setCurrentCacheName(currentCacheName);
        mainVO.setElements(cacheService.listAll(currentCacheName));
    }
}
