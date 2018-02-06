package com.chan.api.binance;

import com.chan.api.AbstractMarketApi;
import com.chan.api.binance.http.AuthenticationInterceptor;
import com.chan.api.binance.model.*;
import com.chan.model.*;
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
                System.out.println(binanceTicker.symbol);
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
    public PlaceOrderResponse placeOrder(Type type, Action action, float price, float quantity) throws IOException {

        String symbol = type2Symbol(type).toUpperCase();
        PlaceOrderResponse response = new PlaceOrderResponse();
        Response<BinancePlaceOrderResponse> binancePlaceOrderResponseResponse = mBinanceApi.placeOrder(
                symbol,
                action == Action.BUY ? OrderSide.BUY : OrderSide.SELL,
                OrderType.LIMIT,
                TimeInForce.GTC,
                quantity,
                price,
                System.currentTimeMillis()).execute();
        response.id = binancePlaceOrderResponseResponse.body().orderId;
        return response;
    }

    @Override
    public void cancelOrder(Type type, String orderId) throws IOException {
        mBinanceApi.cancelOrder(
                type2Symbol(type).toUpperCase(),
                System.currentTimeMillis())
                .execute();
    }

    @Override
    public void withdraw(String address, Type type, float quantity) throws Exception {
        mBinanceApi.withdraw(
                type2Symbol(type),
                address,
                String.valueOf(quantity),
                System.currentTimeMillis())
                .execute();
    }

    @Override
    public Balance fetchBalance() throws Exception {
        Account account = mBinanceApi.getAccount(System.currentTimeMillis()).execute().body();
        Balance balance = new Balance();
        for (Account.BalancesDetail detail : account.balances) {
            Type type = detail.getType();
            if (detail.getFree() >= 0 && detail.getType() != Type.UNKNOWN) {
                Balance.Detail balanceDetail = new Balance.Detail();
                balanceDetail.type = type;
                balanceDetail.amount = detail.getFree();
                balanceDetail.available = true;
                balance.available.put(type, balanceDetail);
            }

            if (detail.getLocked() >= 0 && detail.getType() != Type.UNKNOWN) {
                Balance.Detail balanceDetail = new Balance.Detail();
                balanceDetail.type = type;
                balanceDetail.amount = detail.getFree();
                balanceDetail.available = false;
                balance.frozen.put(type, balanceDetail);
            }
        }

        return balance;
    }

    @Override
    protected String type2Symbol(Type type) {
        if (type == Type.ETH_USDT) {
            return "ethusdt";
        }

        if (type == Type.USDT_ETH) {
            return "usdteth";
        }

        if (type == Type.ETH) {
            return "ETH";
        }

        if (type == Type.USDT) {
            return "USDT";
        }

        if (type == Type.CNY) {
            return "CNY";
        }

        if (type == Type.EOS_USDT) {
            return "eosusdt";
        }

        return null;
    }
}
