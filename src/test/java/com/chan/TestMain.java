package com.chan;

import com.chan.api.MarketApi;
import com.chan.api.binance.BinanceMarketApi;
import com.chan.api.gate.GateMarketApi;
import com.chan.api.huobi.HuoBiMarketApi;
import com.chan.model.Ticker;
import com.chan.model.Type;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public class TestMain {
    public static void main(String[] args) {
        MarketApi huoBiMarketApi = new HuoBiMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");
        MarketApi gateMarketApi = new GateMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");
        MarketApi binanceMarketApi = new BinanceMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");

        try {
            Ticker ticker = huoBiMarketApi.fetchTicker(Type.EOS_USDT);
            System.out.println("huo bi ticker: buy " + ticker.buy + " sell " + ticker.sell);

            ticker = gateMarketApi.fetchTicker(Type.EOS_USDT);
            System.out.println("gate ticker: buy " + ticker.buy + " sell " + ticker.sell);

            ticker = binanceMarketApi.fetchTicker(Type.EOS_USDT);
            System.out.println("binance ticker: buy " + ticker.buy + " sell " + ticker.sell);

            Runtime.getRuntime().exec("say hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
