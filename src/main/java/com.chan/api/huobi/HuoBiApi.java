package com.chan.api.huobi;

import com.chan.api.huobi.model.*;
import com.google.gson.JsonElement;
import retrofit.Call;
import retrofit.http.*;

import java.util.*;

import java.util.List;

public interface HuoBiApi {

    @GET("market/detail/merged")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/39.0.2171.71 Safari/537.36")
    Call<HuoBiTicker> fetchTicker(@Query("symbol") String symbol);

    @POST("v1/order/orders")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
    Call<Long> createOrder(@Body CreateOrderParams request, @QueryMap Map<String, String> signature);

    @POST("v1/order/orders/{id}/place")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
    Call<JsonElement> placeOrder(@Path("id") long id, @QueryMap Map<String, String> signature);

    @POST("v1/order/orders/{id}/submitcancel")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
    Call<JsonElement> cancelOrder(@Path("id") String id);

    @POST("v1/dw/withdraw-virtual/create")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
    Call<JsonElement> withdraw(@Body WithdrawParams params, @QueryMap Map<String, String>
            signature);

    @GET("v1/account/accounts")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
    Call<HuoBiResponse<List<Account>>> fetchAccounts(@QueryMap Map<String, String> signature);

    @GET("v1/account/accounts/{id}/balance")
    @Headers("User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
    Call<HuoBiResponse<HuoBiBalance>> fetchBalance(@Path("id") long accountId, @QueryMap Map<String, String>
            signature);
}