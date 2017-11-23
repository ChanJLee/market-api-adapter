package com.chan.api.huobi.model;

import org.apache.commons.lang.StringUtils;

/**
 * Created by chan on 2017/11/22.
 */
public class HuoBiResponse<T> {
    public String status;
    public T data;

    public boolean isSuccess() {
        return !StringUtils.isBlank(status) && StringUtils.equals(status, "ok");
    }
}
