package com.chan.api.gate.model;

import org.apache.commons.lang.StringUtils;

/**
 * Created by chan on 2017/11/19.
 */
public class GateModel {
    public String result;

    public boolean isSuccess() {
        return !StringUtils.isBlank(result) && StringUtils.equals(result, "true");
    }
}
