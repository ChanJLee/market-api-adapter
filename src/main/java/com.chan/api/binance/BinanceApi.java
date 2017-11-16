package com.chan.api.binance;

import com.chan.api.binance.model.BinanceTicker;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;

import java.util.List;

/**
 * Created by chan on 2017/11/16.
 */
public interface BinanceApi {

    @Headers({"User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36"})
    @GET("api/v1/ticker/allBookTickers")
    Call<List<BinanceTicker>> fetchTickers();
}
