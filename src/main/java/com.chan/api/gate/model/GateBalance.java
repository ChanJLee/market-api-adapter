package com.chan.api.gate.model;

import java.util.Map;

/**
 * Created by chan on 2017/11/19.
 */
public class GateBalance extends GateModel {
    public GateBalanceInternal available;
    public GateBalanceInternal locked;

    public static class GateBalanceInternal {
        private Map<String, String> mTypeAmountPair;

        public float getAmount(String type) {
            return Float.valueOf(mTypeAmountPair.get(type.toUpperCase()));
        }

        public boolean hasContent() {
            return mTypeAmountPair != null && !mTypeAmountPair.isEmpty();
        }
    }
}
