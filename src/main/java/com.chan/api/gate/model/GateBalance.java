package com.chan.api.gate.model;

import java.util.Map;

/**
 * Created by chan on 2017/11/19.
 */
public class GateBalance extends GateModel {
    public Map<String, String> available;
    public Map<String, String> locked;
}
