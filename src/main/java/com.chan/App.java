package com.chan;

import com.chan.api.MarketApi;
import com.chan.api.binance.BinanceMarketApi;
import com.chan.api.gate.GateMarketApi;
import com.chan.api.huobi.HuoBiMarketApi;
import com.chan.common.preference.Preference;
import com.chan.model.Balance;
import com.chan.model.Ticker;
import com.chan.model.Type;

import java.io.File;
import java.io.IOException;

/**
 * Created by chan on 2017/11/22.
 */
public class App {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            return;
        }

        Preference preference = Preference.getInstance(new File(args[0]));

        MarketApi huoBiMarketApi = new HuoBiMarketApi(preference.getString("HUO_BI_ACCESS_KEY"),
                preference.getString("HUO_BI_SECRET_KEY"));
        MarketApi gateMarketApi = new GateMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");
        MarketApi binanceMarketApi = new BinanceMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");

        try {
            Ticker ticker = huoBiMarketApi.fetchTicker(Type.ETH_USDT);
            System.out.println("huo bi ticker: buy " + ticker.buy + " sell " + ticker.sell);

            ticker = gateMarketApi.fetchTicker(Type.ETH_USDT);
            System.out.println("gate ticker: buy " + ticker.buy + " sell " + ticker.sell);

            ticker = binanceMarketApi.fetchTicker(Type.ETH_USDT);
            System.out.println("binance ticker: buy " + ticker.buy + " sell " + ticker.sell);

            Balance balance = huoBiMarketApi.fetchBalance();
            System.out.println("huo bi balance: " + balance.frozen.size() + " " + balance.available.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
