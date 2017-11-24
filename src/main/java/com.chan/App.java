package com.chan;

import com.chan.api.MarketApi;
import com.chan.api.binance.BinanceMarketApi;
import com.chan.api.gate.GateMarketApi;
import com.chan.api.huobi.HuoBiMarketApi;
import com.chan.common.log.Logger;
import com.chan.common.preference.Preference;
import com.chan.model.*;

import java.io.File;
import java.util.*;

/**
 * Created by chan on 2017/11/22.
 */
public class App {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            return;
        }

        Preference preference = Preference.getInstance(new File(args[0]));
        Logger.log("huo bi access key", preference.getString("HUO_BI_ACCESS_KEY"));
        Logger.log("huo bi secret key", preference.getString("HUO_BI_SECRET_KEY"));
        Logger.log("binance access key", preference.getString("BINANCE_ACCESS_KEY"));
        Logger.log("binance secret key", preference.getString("BINANCE_SECRET_KEY"));
        Logger.log("gate access key", preference.getString("GATE_ACCESS_KEY"));
        Logger.log("gate secret key", preference.getString("GATE_SECRET_KEY"));

        MarketApi huoBiMarketApi = new HuoBiMarketApi(preference.getString("HUO_BI_ACCESS_KEY"),
                preference.getString("HUO_BI_SECRET_KEY"));
        MarketApi gateMarketApi = new GateMarketApi(preference.getString("GATE_ACCESS_KEY"),
                preference.getString("GATE_SECRET_KEY"));
        MarketApi binanceMarketApi = new BinanceMarketApi(preference.getString("BINANCE_ACCESS_KEY"),
                preference.getString("BINANCE_SECRET_KEY"));

        try {
            Ticker ticker = huoBiMarketApi.fetchTicker(Type.ETH_USDT);
            System.out.println("huo bi ticker: buy " + ticker.buy + " sell " + ticker.sell);

            ticker = gateMarketApi.fetchTicker(Type.ETH_USDT);
            System.out.println("gate ticker: buy " + ticker.buy + " sell " + ticker.sell);

            ticker = binanceMarketApi.fetchTicker(Type.ETH_USDT);
            System.out.println("binance ticker: buy " + ticker.buy + " sell " + ticker.sell);

            Balance balance = huoBiMarketApi.fetchBalance();
            System.out.println("huo bi balance");
            System.out.println("frozen");
            for (Map.Entry<Type, Balance.Detail> entry : balance.frozen.entrySet()) {
                System.out.println("type: " + entry.getKey() + " " + entry.getValue().available + " " + entry
                        .getValue().amount + " " + entry.getValue().type);
            }
            System.out.println("available");
            for (Map.Entry<Type, Balance.Detail> entry : balance.available.entrySet()) {
                System.out.println("type: " + entry.getKey() + " " + entry.getValue().available + " " + entry
                        .getValue().amount + " " + entry.getValue().type);
            }

            balance = binanceMarketApi.fetchBalance();
            System.out.println("binance balance");
            System.out.println("frozen");
            for (Map.Entry<Type, Balance.Detail> entry : balance.frozen.entrySet()) {
                System.out.println("type: " + entry.getKey() + " " + entry.getValue().available + " " + entry
                        .getValue().amount + " " + entry.getValue().type);
            }
            System.out.println("available");
            for (Map.Entry<Type, Balance.Detail> entry : balance.available.entrySet()) {
                System.out.println("type: " + entry.getKey() + " " + entry.getValue().available + " " + entry
                        .getValue().amount + " " + entry.getValue().type);
            }

            balance = gateMarketApi.fetchBalance();
            System.out.println("gate balance");
            System.out.println("frozen");
            for (Map.Entry<Type, Balance.Detail> entry : balance.frozen.entrySet()) {
                System.out.println("type: " + entry.getKey() + " " + entry.getValue().available + " " + entry
                        .getValue().amount + " " + entry.getValue().type);
            }
            System.out.println("available");
            for (Map.Entry<Type, Balance.Detail> entry : balance.available.entrySet()) {
                System.out.println("type: " + entry.getKey() + " " + entry.getValue().available + " " + entry
                        .getValue().amount + " " + entry.getValue().type);
            }

            PlaceOrderResponse placeOrderResponse = gateMarketApi.placeOrder(Type.ETH, Action.BUY, 1, 1);
            System.out.println("gate buy: " + placeOrderResponse.id);
//            PlaceOrderResponse placeOrderResponse = huoBiMarketApi.placeOrder(Type.ETH, Action.BUY, 1, 1);
//            System.out.println("huo bi buy: " + placeOrderResponse.id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
