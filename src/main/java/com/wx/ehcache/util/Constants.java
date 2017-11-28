package com.wx.ehcache.util;

public interface Constants {
    int DEFAULT_MAX_ELEMENTS_IN_MEMERY = 1000;

    boolean DEFAULT_OVERFLOW_TO_DISK = false;

    boolean DEFAULT_EXTERNAL = true;

    int DEFAULT_TIME_TO_LIVE_SECONDS = 500;

    int DEFAULT_TIME_TO_IDLE_SECONDS = 100;

    int DEFAULT_MAX_ENTRIES_LOCAL_HEAP = 100;

    String CACHE_EVENT_LISTENER_FACTORY_CLASS = "net.sf.ehcache.distribution.RMICacheReplicatorFactory";
}
