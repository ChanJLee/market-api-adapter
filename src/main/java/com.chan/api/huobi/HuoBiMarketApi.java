package com.chan.api.huobi;

import com.chan.api.MarketApi;
import com.chan.api.huobi.model.HuoBiTicker;
import com.chan.model.Ticker;
import com.chan.model.Type;
import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public class HuoBiMarketApi implements MarketApi {
    private HuoBiApi mHuoBiApi;
    private String mAccessKey;
    private String mSecretKey;

    public HuoBiMarketApi(String accessKey, String secretKey) {
        mAccessKey = accessKey;
        mSecretKey = secretKey;
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.huobi.pro/")
                .build();
        mHuoBiApi = retrofit.create(HuoBiApi.class);
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
