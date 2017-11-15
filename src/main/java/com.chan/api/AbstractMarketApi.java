package com.chan.api;

import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by chan on 2017/11/15.
 */
public abstract class AbstractMarketApi implements MarketApi {
    protected String mAccessKey;
    protected String mSecretKey;
    protected String mBaseUrl;
    private Retrofit mRetrofit;

    public AbstractMarketApi(String accessKey, String secretKey, String baseUrl) {
        mAccessKey = accessKey;
        mSecretKey = secretKey;
        mBaseUrl = baseUrl;

        OkHttpClient okHttpClient = new OkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    protected <T> T createApi(Class<T> apiClazz) {
        return mRetrofit.create(apiClazz);
    }
}
