package com.chan.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chan on 2017/11/19.
 */
public class Balance {

    /**
     * 可以流通的
     */
    public Map<Type, Detail> available = new HashMap<>();

    /**
     * 被冻结的
     */
    public Map<Type, Detail> frozen = new HashMap<>();

    /**
     * 账户细节
     */
    public static class Detail {
        /**
         * 当前属于的币种
         */
        public Type type;
        /**
         * 是否被冻结
         */
        public boolean available = true;
        /**
         * 数量
         */
        public float amount = 0f;
    }
}
