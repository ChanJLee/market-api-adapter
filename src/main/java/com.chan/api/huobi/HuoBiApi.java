package com.chan.api.huobi;

import com.chan.api.huobi.model.CreateOrderParams;
import com.chan.api.huobi.model.HuoBiTicker;
import com.google.gson.JsonElement;
import retrofit.Call;
import retrofit.http.*;

public interface HuoBiApi {

    @GET("market/detail/merged")
    Call<HuoBiTicker> fetchTicker(@Query("symbol") String symbol);

    @POST("v1/order/orders")
    Call<Long> createOrder(@Body CreateOrderParams request);

    @POST("v1/order/orders/{id}/place")
    Call<JsonElement> placeOrder(@Path("id") long id);
}