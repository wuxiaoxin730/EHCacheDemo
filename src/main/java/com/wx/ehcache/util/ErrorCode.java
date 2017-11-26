package com.wx.ehcache.util;

public enum ErrorCode {
    CACHE_ALREADY_EXIST("E0001", "Cache已经存在"),
    CACHE_NOT_EXIST("E0002", "Cache不存在");

    private String code;

    private String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
