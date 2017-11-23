package com.chan.api.huobi.model;

import com.chan.model.Type;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by chan on 2017/11/19.
 */
public class HuoBiBalance {
    public List<HuoBiBalanceDetail> list;

    public static class HuoBiBalanceDetail {
        private String currency;
        private String type;
        private String balance;

        public boolean isAvailable() {
            return !StringUtils.isBlank(type) && !StringUtils.equals(type.toLowerCase(), "frozen");
        }

        public float getAmount() {
            return Float.valueOf(balance);
        }

        public Type getType() {
            if (StringUtils.isBlank(currency)) {
                return Type.UNKNOWN;
            }

            String lowerCaseType = currency.toLowerCase();
            if (StringUtils.equals(lowerCaseType, "eth")) {
                return Type.ETH;
            }

            if (StringUtils.equals(lowerCaseType, "usdt")) {
                return Type.USDT;
            }

            if (StringUtils.equals(lowerCaseType, "cny")) {
                return Type.CNY;
            }

            return Type.UNKNOWN;
        }
    }
}
