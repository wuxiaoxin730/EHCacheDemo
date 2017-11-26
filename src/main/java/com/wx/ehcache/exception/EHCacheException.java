package com.wx.ehcache.exception;

import com.wx.ehcache.util.ErrorCode;

public class EHCacheException extends Exception {
    private ErrorCode errorCode;

    public EHCacheException() {
        super();
    }

    public EHCacheException(String message) {
        super(message);
    }

    public EHCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public EHCacheException(Throwable cause) {
        super(cause);
    }

    protected EHCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EHCacheException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
