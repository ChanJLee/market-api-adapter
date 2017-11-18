package com.chan.api.gate;

import com.chan.api.AbstractMarketApi;
import com.chan.api.gate.model.GatePlaceOrderResponse;
import com.chan.api.gate.model.GateTicker;
import com.chan.api.gate.utils.MiscUtils;
import com.chan.model.PlaceOrderResponse;
import com.chan.model.Ticker;
import com.chan.model.Type;
import org.apache.commons.lang.StringUtils;
import retrofit.Response;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
    public PlaceOrderResponse placeOrder(Type type, float price, float quantity) throws IOException {
        PlaceOrderResponse response = new PlaceOrderResponse();
        Map<String, String> map = new HashMap<>();
        map.put("currencyPair", type2Symbol(type));
        map.put("rate", String.valueOf(price));
        map.put("amount", String.valueOf(quantity));

        try {
            Response<GatePlaceOrderResponse> gatePlaceOrderResponseResponse = mGateApi.placeOrder(map, mAccessKey,
                    MiscUtils.signature(map, mSecretKey)).execute();
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

    }

    @Override
    protected String type2Symbol(Type type) {
        if (type == Type.ETH_USDT) {
            return "eth_usdt";
        }

        if (type == Type.USDT_ETH) {
            return "usdt_eth";
        }

        return null;
    }
}
