package com.chan.api.huobi;

import com.chan.api.huobi.model.*;
import com.google.gson.JsonElement;
import retrofit.Call;
import retrofit.http.*;

import java.util.List;

public interface HuoBiApi {

    @GET("market/detail/merged")
    Call<HuoBiTicker> fetchTicker(@Query("symbol") String symbol);

    @POST("v1/order/orders")
    Call<Long> createOrder(@Body CreateOrderParams request);

    @POST("v1/order/orders/{id}/place")
    Call<JsonElement> placeOrder(@Path("id") long id);

    @POST("v1/order/orders/{id}/submitcancel")
    Call<JsonElement> cancelOrder(@Path("id") String id);

    @POST("v1/dw/withdraw-virtual/create")
    Call<JsonElement> withdraw(@Body WithdrawParams params);

    @GET("v1/account/accounts")
    Call<List<Account>> fetchAccounts();

    @GET("v1/account/accounts/{id}/balance")
    Call<HuoBiBalance> fetchBalance(@Path("id") long accountId);
}