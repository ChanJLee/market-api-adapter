package com.chan.api.huobi;

import com.chan.api.AbstractMarketApi;
import com.chan.api.huobi.model.HuoBiTicker;
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
        if (StringUtils.isBlank(huoBiTicker.status) || !StringUtils.equals("ok", huoBiTicker.status.toLowerCase())) {
            throw new IOException("huo bi: fetch ticker failed");
        }

        ticker.buy = huoBiTicker.getBuy();
        ticker.sell = huoBiTicker.getSell();

        return ticker;
    }

    @Override
    public PlaceOrderResponse placeOrder(Type type, float price, float num) {
        return null;
    }

    @Override
    protected String type2Symbol(Type type) {
        if (type == Type.ETH_USDT) {
            return "ethusdt";
        }
        return null;
    }
}
