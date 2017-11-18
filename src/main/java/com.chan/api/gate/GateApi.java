package com.chan.api.gate;

import com.chan.api.gate.model.GatePlaceOrderResponse;
import com.chan.api.gate.model.GateTicker;
import retrofit.Call;
import retrofit.http.*;

import java.util.Map;

/**
 * Created by chan on 2017/11/16.
 */
public interface GateApi {

    @GET("api2/1/ticker/{symbol}")
    Call<GateTicker> fetchTicker(@Path("symbol") String symbol);

    @FormUrlEncoded
    @POST("api2/1/private/buy")
    Call<GatePlaceOrderResponse> placeOrder(@FieldMap Map<String, String> entries,
                                            @Header("Key") String key,
                                            @Header("Sign") String signature);
}
