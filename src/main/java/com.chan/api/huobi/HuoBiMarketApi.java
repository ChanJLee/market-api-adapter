package com.chan.api.huobi;

import com.chan.api.AbstractMarketApi;
import com.chan.api.huobi.model.*;
import com.chan.api.huobi.utils.MiscUtils;
import com.chan.common.log.Logger;
import com.chan.model.*;
import retrofit.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chan on 2017/11/14.
 */
public class HuoBiMarketApi extends AbstractMarketApi {
    private static final String TAG = "HuoBiMarketApi";

    private HuoBiApi mHuoBiApi;

    public HuoBiMarketApi(String accessKey, String secretKey) {
        super(accessKey, secretKey, "https://api.huobi.pro/");
        mHuoBiApi = createApi(HuoBiApi.class);
    }

    @Override
    public Ticker fetchTicker(Type type) throws IOException {

        Ticker ticker = new Ticker();
        String symbol = type2Symbol(type);

        Response<HuoBiTicker> response = mHuoBiApi.fetchTicker(symbol).execute();
        HuoBiTicker huoBiTicker = response.body();
        if (!huoBiTicker.isSuccess()) {
            throw new IOException("huo bi: fetch ticker failed");
        }

        ticker.buy = huoBiTicker.getBuy();
        ticker.sell = huoBiTicker.getSell();

        return ticker;
    }

    @Override
    public PlaceOrderResponse placeOrder(Type type, Action action, float price, float quantity) throws IOException {
        PlaceOrderResponse response = new PlaceOrderResponse();

        CreateOrderParams createOrderParams = new CreateOrderParams();
        createOrderParams.accountId = String.valueOf(fetchAccountId());
        createOrderParams.amount = String.valueOf(quantity);
        createOrderParams.price = String.valueOf(price);
        createOrderParams.symbol = type2Symbol(type);
        createOrderParams.type = action == Action.BUY ?
                CreateOrderParams.OrderType.BUY_LIMIT : CreateOrderParams.OrderType.SELL_LIMIT;
        Map<String, String> signature = new HashMap<>();
        MiscUtils.signature(
                mAccessKey,
                mSecretKey,
                "POST",
                "api.huobi.pro",
                "/v1/order/orders",
                signature
        );

        long orderId = mHuoBiApi.createOrder(createOrderParams, signature).execute().body();
        response.id = String.valueOf(orderId);
        signature = new HashMap<>();
        MiscUtils.signature(
                mAccessKey,
                mSecretKey,
                "POST",
                "api.huobi.pro",
                String.format("/v1/order/orders/%d/place", orderId),
                signature
        );
        mHuoBiApi.placeOrder(orderId, signature).execute();
        return response;
    }

    @Override
    public void cancelOrder(Type type, String orderId) throws IOException {
        Map<String, String> signature = new HashMap<>();
        MiscUtils.signature(
                mAccessKey,
                mSecretKey,
                "POST",
                "api.huobi.pro",
                String.format("/v1/order/orders/%s/submitcancel", orderId),
                signature
        );
        mHuoBiApi.cancelOrder(orderId).execute();
    }

    @Override
    public void withdraw(String address, Type type, float quantity) throws Exception {
        WithdrawParams params = new WithdrawParams();
        params.address = address;
        params.amount = String.valueOf(quantity);
        params.currency = type2Symbol(type);
        Map<String, String> signature = new HashMap<>();
        MiscUtils.signature(
                mAccessKey,
                mSecretKey,
                "POST",
                "api.huobi.pro",
                "/v1/dw/withdraw-virtual/create",
                signature
        );
        mHuoBiApi.withdraw(params, signature).execute();
    }

    @Override
    public Balance fetchBalance() throws Exception {
        long accountId = fetchAccountId();
        Logger.log(TAG, "account id: " + accountId);
        Map<String, String> signature = new HashMap<>();
        MiscUtils.signature(
                mAccessKey,
                mSecretKey,
                "GET",
                "api.huobi.pro",
                String.format("/v1/account/accounts/%s/balance", accountId),
                signature
        );

        HuoBiResponse<HuoBiBalance> response = mHuoBiApi.fetchBalance(fetchAccountId(), signature).execute()
                .body();
        if (!response.isSuccess()) {
            throw new IOException("huo bi: fetch balance failed");
        }

        Balance balance = new Balance();
        for (HuoBiBalance.HuoBiBalanceDetail detail : response.data.list) {
            if (detail.getType() == Type.UNKNOWN) {
                continue;
            }

            Balance.Detail balanceDetail = new Balance.Detail();
            balanceDetail.available = detail.isAvailable();
            balanceDetail.amount = detail.getAmount();
            balanceDetail.type = detail.getType();
            Map<Type, Balance.Detail> target = detail.isAvailable() ? balance.available : balance.frozen;
            target.put(balanceDetail.type, balanceDetail);
        }

        return balance;
    }

    private long fetchAccountId() throws IOException {
        Map<String, String> signature = new HashMap<>();
        MiscUtils.signature(
                mAccessKey,
                mSecretKey,
                "GET",
                "api.huobi.pro",
                "/v1/account/accounts",
                signature
        );

        HuoBiResponse<List<Account>> wrapper = mHuoBiApi.fetchAccounts(signature).execute().body();
        if (!wrapper.isSuccess()) {
            throw new IOException(("fetch account failed"));
        }
        List<Account> accounts = wrapper.data;
        if (accounts == null || accounts.isEmpty()) {
            throw new IOException("fetch account id failed");
        }
        return accounts.get(0).id;
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
            return "eth";
        }

        if (type == Type.USDT) {
            return "usdt";
        }

        if (type == Type.CNY) {
            return "cny";
        }

        if (type == Type.EOS_USDT) {
            return "eosusdt";
        }

        return null;
    }
}
