package com.chan.api.huobi.model;

import org.apache.commons.lang.StringUtils;

/**
 * Created by chan on 2017/11/19.
 */
public class HuoBiModel {
    public String status;

    public boolean isSuccess() {
        return !StringUtils.isEmpty(status) && StringUtils.equals(status.toLowerCase(), "ok");
    }
}
