package com.chan.api.binance;

import com.chan.api.binance.http.BinanceApiConstants;
import com.chan.api.binance.model.*;
import com.google.gson.JsonElement;
import retrofit.Call;
import retrofit.http.*;

import java.util.List;

/**
 * Created by chan on 2017/11/16.
 */
public interface BinanceApi {

    @GET("api/v1/ticker/allBookTickers")
    Call<List<BinanceTicker>> fetchTickers();

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("api/v3/order")
    Call<BinancePlaceOrderResponse> placeOrder(@Query("symbol") String symbol,
                                               @Query("side") OrderSide side,
                                               @Query("type") OrderType type,
                                               @Query("timeInForce") TimeInForce timeInForce,
                                               @Query("quantity") float quantity,
                                               @Query("price") float price,
                                               @Query("timestamp") long timestamp);

    @DELETE("api/v3/order")
    Call<JsonElement> cancelOrder(@Query("symbol") String symbol, @Query("timestamp") long timestamp);

    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @POST("/wapi/v1/withdraw.html")
    Call<JsonElement> withdraw(@Query("asset") String asset,
                               @Query("address") String address,
                               @Query("amount") String amount,
                               @Query("timestamp") long timestamp);


    @Headers(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED_HEADER)
    @GET("/api/v3/account")
    Call<Account> getAccount(@Query("timestamp") Long timestamp);
}
