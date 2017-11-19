package com.chan.api.huobi;

import com.chan.api.AbstractMarketApi;
import com.chan.api.huobi.model.CreateOrderParams;
import com.chan.api.huobi.model.HuoBiTicker;
import com.chan.api.huobi.model.WithdrawParams;
import com.chan.model.Action;
import com.chan.model.PlaceOrderResponse;
import com.chan.model.Ticker;
import com.chan.model.Type;
import org.apache.commons.lang.StringUtils;
import retrofit.Response;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public class HuoBiMarketApi extends AbstractMarketApi {
    private HuoBiApi mHuoBiApi;
    private String mAccountId;

    public HuoBiMarketApi(String accessKey, String secretKey, String accountId) {
        super(accessKey, secretKey, "https://api.huobi.pro/");
        mHuoBiApi = createApi(HuoBiApi.class);
        mAccountId = accountId;
    }

    @Override
    public Ticker fetchTicker(Type type) throws IOException {

        Ticker ticker = new Ticker();
        String symbol = type2Symbol(type);

        Response<HuoBiTicker> response = mHuoBiApi.fetchTicker(symbol).execute();
        HuoBiTicker huoBiTicker = response.body();
        if (StringUtils.isBlank(huoBiTicker.status) || !StringUtils.equals("ok", huoBiTicker.status.toLowerCase())) {
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
        createOrderParams.accountId = mAccountId;
        createOrderParams.amount = String.valueOf(quantity);
        createOrderParams.price = String.valueOf(price);
        createOrderParams.symbol = type2Symbol(type);
        createOrderParams.type = action == Action.BUY ?
                CreateOrderParams.OrderType.BUY_LIMIT : CreateOrderParams.OrderType.SELL_LIMIT;

        long orderId = mHuoBiApi.createOrder(createOrderParams).execute().body();
        response.id = String.valueOf(orderId);
        mHuoBiApi.placeOrder(orderId).execute();
        return response;
    }

    @Override
    public void cancelOrder(Type type, String orderId) throws IOException {
        mHuoBiApi.cancelOrder(orderId);
    }

    @Override
    public void withdraw(String address, Type type, float quantity) throws Exception {
        WithdrawParams params = new WithdrawParams();
        params.address = address;
        params.amount = String.valueOf(quantity);
        params.currency = type2Symbol(type);
        mHuoBiApi.withdraw(params).execute();
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

        return null;
    }
}
