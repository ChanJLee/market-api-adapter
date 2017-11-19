package com.chan.api.gate;

import com.chan.api.AbstractMarketApi;
import com.chan.api.gate.model.GateBalance;
import com.chan.api.gate.model.GatePlaceOrderResponse;
import com.chan.api.gate.model.GateTicker;
import com.chan.api.gate.utils.MiscUtils;
import com.chan.model.*;
import org.apache.commons.lang.StringUtils;
import retrofit.Call;
import retrofit.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chan on 2017/11/16.
 */
public class GateMarketApi extends AbstractMarketApi {

    private GateApi mGateApi;

    public GateMarketApi(String accessKey, String secretKey) {
        super(accessKey, secretKey, "http://data.gate.io");
        mGateApi = createApi(GateApi.class);
    }

    @Override
    public Ticker fetchTicker(Type type) throws IOException {

        Ticker ticker = new Ticker();
        String symbol = type2Symbol(type);

        Response<GateTicker> response = mGateApi.fetchTicker(symbol).execute();
        GateTicker gateTicker = response.body();
        if (StringUtils.isBlank(gateTicker.result) || !StringUtils.equals("true", gateTicker.result.toLowerCase())) {
            throw new IOException("gate: fetch ticker failed");
        }

        ticker.sell = gateTicker.lowestAsk;
        ticker.buy = gateTicker.highestBid;

        return ticker;
    }

    @Override
    public PlaceOrderResponse placeOrder(Type type, Action action, float price, float quantity) throws IOException {
        PlaceOrderResponse response = new PlaceOrderResponse();
        Map<String, String> map = new HashMap<>();
        map.put("currencyPair", type2Symbol(type));
        map.put("rate", String.valueOf(price));
        map.put("amount", String.valueOf(quantity));

        try {
            Call<GatePlaceOrderResponse> call = action == Action.BUY ?
                    mGateApi.buy(map, mAccessKey, MiscUtils.signature(map, mSecretKey)) :
                    mGateApi.sell(map, mAccessKey, MiscUtils.signature(map, mSecretKey));

            Response<GatePlaceOrderResponse> gatePlaceOrderResponseResponse = call.execute();
            response.id = gatePlaceOrderResponseResponse.body().orderNumber;
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("gate place order failed: " + e.getMessage());
        }
    }

    @Override
    public void cancelOrder(Type type, String orderId) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderNumber", orderId);
        map.put("currencyPair", type2Symbol(type));

        try {
            mGateApi.cancelOrder(map, mAccessKey, MiscUtils.signature(map, mSecretKey)).execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("gate cancel order failed: " + e.getMessage());
        }
    }

    @Override
    public void withdraw(String address, Type type, float quantity) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("currency", type2Symbol(type));
        map.put("amount", String.valueOf(quantity));
        map.put("address", address);

        mGateApi.withdraw(map, mAccessKey, MiscUtils.signature(map, mSecretKey)).execute();
    }

    @Override
    public Balance fetchBalance() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("api_key", mAccessKey);
        String sign = MiscUtils.buildMysignV1(map, mSecretKey);
        map.put("sign", sign);

        GateBalance gateBalance = mGateApi.fetchBalance(map, mAccessKey, MiscUtils.signature(map, mSecretKey))
                .execute().body();

        if (gateBalance == null || !gateBalance.isSuccess()) {
            throw new IOException("gate fetch balance failed");
        }
        Balance balance = new Balance();

        if (gateBalance.available.hasContent()) {
            addBalance(balance.available, gateBalance.available, true, Type.CNY, Type.ETH, Type.USDT);
        }

        if (gateBalance.locked.hasContent()) {
            addBalance(balance.frozen, gateBalance.locked, false, Type.CNY, Type.ETH, Type.USDT);
        }

        return balance;
    }

    private void addBalance(Map<Type, Balance.Detail> target,
                            GateBalance.GateBalanceInternal gateBalanceInternal,
                            boolean available,
                            Type... types) {
        for (Type type : types) {
            try {
                Balance.Detail detail = new Balance.Detail();
                detail.type = type;
                detail.available = available;
                detail.amount = gateBalanceInternal.getAmount(type2Symbol(detail.type));
                target.put(detail.type, detail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String type2Symbol(Type type) {
        if (type == Type.ETH_USDT) {
            return "eth_usdt";
        }

        if (type == Type.USDT_ETH) {
            return "usdt_eth";
        }

        if (type == Type.USDT) {
            return "usdt";
        }

        if (type == Type.ETH) {
            return "eth";
        }

        if (type == Type.CNY) {
            return "cny";
        }

        return null;
    }
}
