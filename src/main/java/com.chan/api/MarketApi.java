package com.chan.api;

import com.chan.model.PlaceOrderResponse;
import com.chan.model.Ticker;
import com.chan.model.Type;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public interface MarketApi {
    /**
     * 获取市场信息
     *
     * @param type 类型
     * @return
     * @throws IOException
     */
    Ticker fetchTicker(Type type) throws IOException;

    /**
     * 下单 buy
     *
     * @param type
     * @param price    买入价格
     * @param quantity 买入数量
     * @return
     * @throws IOException
     */
    PlaceOrderResponse placeOrder(Type type, float price, float quantity) throws IOException;

    /**
     * 撤销订单
     *
     * @param type
     * @param orderId
     * @throws IOException
     */
    void cancelOrder(Type type, String orderId) throws IOException;

    /**
     * 提现
     *
     * @param address  地址
     * @param type
     * @param quantity 数量
     * @throws Exception
     */
    void withdraw(String address, Type type, float quantity) throws Exception;
}
