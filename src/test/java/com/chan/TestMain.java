package com.chan;

import com.chan.api.huobi.HuoBiMarketApi;
import com.chan.model.Ticker;
import com.chan.model.Type;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public class TestMain {
    public static void main(String[] args) {
        HuoBiMarketApi api = new HuoBiMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");
        try {
            Ticker ticker = api.fetchTicker(Type.ETH_TO_USDT);
            System.out.println(ticker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
