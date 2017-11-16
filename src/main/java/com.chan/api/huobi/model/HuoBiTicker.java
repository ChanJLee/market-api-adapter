package com.chan.api.huobi.model;

import java.util.List;

/**
 * Created by chan on 2017/11/15.
 */
public class HuoBiTicker {
    public String status;
    public HuoBiTickerInternal tick;

    public float getBuy() {
        return tick.getBuy();
    }

    public float getSell() {
        return tick.getSell();
    }

    private static class HuoBiTickerInternal {
        List<Float> bid;
        List<Float> ask;

        float getBuy() {
            return bid == null || bid.isEmpty() ? -1f : bid.get(0);
        }

        float getSell() {
            return ask == null || ask.isEmpty() ? -1f : ask.get(0);
        }
    }
}
