package com.chan.api.huobi;

import com.chan.api.AbstractMarketApi;
import com.chan.api.MarketApi;
import com.chan.api.huobi.model.HuoBiTicker;
import com.chan.model.Ticker;
import com.chan.model.Type;
import retrofit.Response;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public class HuoBiMarketApi extends AbstractMarketApi implements MarketApi {
    private HuoBiApi mHuoBiApi;

    public HuoBiMarketApi(String accessKey, String secretKey) {
        super(accessKey, secretKey, "https://api.huobi.pro/");
        mHuoBiApi = createApi(HuoBiApi.class);
    }

    @Override
    public Ticker fetchTicker(Type pair) throws IOException {

        Ticker ticker = new Ticker();
        if (pair == Type.ETH_TO_USDT) {
            Response<HuoBiTicker> response = mHuoBiApi.fetchTicker("ethusdt").execute();
            HuoBiTicker huoBiTicker = response.body();
            ticker.buy = huoBiTicker.getBuy();
            ticker.sell = huoBiTicker.getSell();
        }

        return ticker;
    }
}
