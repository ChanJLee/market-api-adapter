package com.chan.api.binance;

import com.chan.api.AbstractMarketApi;
import com.chan.api.binance.http.AuthenticationInterceptor;
import com.chan.api.binance.model.*;
import com.chan.model.PlaceOrderResponse;
import com.chan.model.Ticker;
import com.chan.model.Type;
import com.squareup.okhttp.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import retrofit.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by chan on 2017/11/16.
 */
public class BinanceMarketApi extends AbstractMarketApi {
    private BinanceApi mBinanceApi;

    public BinanceMarketApi(String accessKey, String secretKey) {
        super(accessKey, secretKey, "https://www.binance.com");
        mBinanceApi = createApi(BinanceApi.class);
    }

    @Override
    protected OkHttpClient createOkHttpClient() {
        OkHttpClient okHttpClient = super.createOkHttpClient();
        okHttpClient.interceptors().add(new AuthenticationInterceptor(mAccessKey, mSecretKey));
        return okHttpClient;
    }

    @Override
    public Ticker fetchTicker(Type pair) throws IOException {
        Ticker ticker = new Ticker();

        String symbol = type2Symbol(pair);
        Response<List<BinanceTicker>> response = mBinanceApi.fetchTickers().execute();
        List<BinanceTicker> tickers = response.body();
        if (tickers == null || tickers.isEmpty()) {
            throw new IOException("binance: fetch ticker failed");
        }

        try {
            for (BinanceTicker binanceTicker : tickers) {
                if (!StringUtils.isBlank(binanceTicker.symbol) &&
                        StringUtils.equals(binanceTicker.symbol.toLowerCase(), symbol)) {
                    ticker.sell = Float.parseFloat(binanceTicker.askPrice);
                    ticker.buy = Float.parseFloat(binanceTicker.bidPrice);
                    return ticker;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IOException("binance: fetch ticker failed");
    }

    @Override
    public PlaceOrderResponse placeOrder(Type type, float price, float quantity) throws IOException {

        String symbol = type2Symbol(type);
        PlaceOrderResponse response = new PlaceOrderResponse();
        Response<BinancePlaceOrderResponse> binancePlaceOrderResponseResponse = mBinanceApi.placeOrder(
                symbol,
                OrderSide.BUY,
                OrderType.LIMIT,
                TimeInForce.GTC,
                quantity,
                price,
                System.currentTimeMillis()).execute();
        response.id = binancePlaceOrderResponseResponse.body().orderId;
        return response;
    }

    @Override
    protected String type2Symbol(Type type) {
        if (type == Type.ETH_USDT) {
            return "ethusdt";
        }

        return null;
    }
}