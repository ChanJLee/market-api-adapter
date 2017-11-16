package com.chan.api.gate;

import com.chan.api.gate.model.GateTicker;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by chan on 2017/11/16.
 */
public interface GateApi {

    @GET("api2/1/ticker/{symbol}")
    Call<GateTicker> fetchTicker(@Path("symbol") String symbol);
}
