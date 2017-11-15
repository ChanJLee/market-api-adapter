package com.chan.api.huobi;

import com.chan.api.huobi.model.HuoBiTicker;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface HuoBiApi {

    @GET("market/detail/merged")
    Call<HuoBiTicker> fetchTicker(@Query("symbol") String symbol);
}