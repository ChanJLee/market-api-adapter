package com.chan.api.binance;

import com.chan.api.binance.http.BinanceApiConstants;
import com.chan.api.binance.model.*;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by chan on 2017/11/16.
 */
public interface BinanceApi {

    @GET("api/v1/ticker/allBookTickers")
    Call<List<BinanceTicker>> fetchTickers();

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/api/v3/order")
    Call<BinancePlaceOrderResponse> placeOrder(@Query("symbol") String symbol,
                                               @Query("side") OrderSide side,
                                               @Query("type") OrderType type,
                                               @Query("timeInForce") TimeInForce timeInForce,
                                               @Query("quantity") float quantity,
                                               @Query("price") float price,
                                               @Query("timestamp") long timestamp);
}
