package com.chan.api.binance.model;


import com.chan.model.Type;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Account information.
 */
public class Account {

    public List<BalancesDetail> balances;

    public static class BalancesDetail {
        private String asset;
        private String free;
        private String locked;

        public float getFree() {
            return Float.valueOf(free);
        }

        public float getLocked() {
            return Float.valueOf(locked);
        }

        public Type getType() {
            if (StringUtils.equals(asset, "ETH")) {
                return Type.ETH;
            }

            if (StringUtils.equals(asset, "CNY")) {
                return Type.CNY;
            }

            if (StringUtils.equals(asset, "USDT")) {
                return Type.USDT;
            }

            return Type.UNKNOWN;
        }
    }
}
