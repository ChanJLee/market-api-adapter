package com.chan.api.gate;

import com.chan.api.gate.model.GateBalance;
import com.chan.api.gate.model.GatePlaceOrderResponse;
import com.chan.api.gate.model.GateTicker;
import com.google.gson.JsonElement;
import retrofit.Call;
import retrofit.http.*;

import java.util.Map;

/**
 * Created by chan on 2017/11/16.
 */
public interface GateApi {

    @GET("api2/1/ticker/{symbol}")
    Call<GateTicker> fetchTicker(@Path("symbol") String symbol);

    @POST("api2/1/private/buy")
    @FormUrlEncoded
    Call<GatePlaceOrderResponse> buy(@FieldMap Map<String, String> entries,
                                     @Header("Key") String key,
                                     @Header("Sign") String signature);

    @POST("api2/1/private/sell")
    @FormUrlEncoded
    Call<GatePlaceOrderResponse> sell(@FieldMap Map<String, String> entries,
                                      @Header("Key") String key,
                                      @Header("Sign") String signature);

    @POST("api2/1/private/cancelOrder")
    @FormUrlEncoded
    Call<JsonElement> cancelOrder(@FieldMap Map<String, String> entries,
                                  @Header("Key") String key,
                                  @Header("Sign") String signature);

    @POST("api2/1/private/withdraw")
    @FormUrlEncoded
    Call<JsonElement> withdraw(@FieldMap Map<String, String> entries,
                               @Header("Key") String key,
                               @Header("Sign") String signature);

    @POST("api2/1/private/balances")
    @FormUrlEncoded
    Call<GateBalance> fetchBalance(@FieldMap Map<String, String> entries,
                                   @Header("Key") String key,
                                   @Header("Sign") String signature);
}
