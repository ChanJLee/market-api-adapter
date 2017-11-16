package com.chan.api.gate;

import com.chan.api.AbstractMarketApi;
import com.chan.api.gate.model.GateTicker;
import com.chan.model.Ticker;
import com.chan.model.Type;
import org.apache.commons.lang.StringUtils;
import retrofit.Response;

import java.io.IOException;

/**
 * Created by chan on 2017/11/16.
 */
public class GateMarketApi extends AbstractMarketApi {

    private GateApi mGateApi;

    public GateMarketApi(String accessKey, String secretKey) {
        super(accessKey, secretKey, "http://data.gate.io");
        mGateApi = createApi(GateApi.class);
    }

    @Override
    public Ticker fetchTicker(Type type) throws IOException {

        Ticker ticker = new Ticker();
        String symbol = type2Symbol(type);

        Response<GateTicker> response = mGateApi.fetchTicker(symbol).execute();
        GateTicker gateTicker = response.body();
        if (StringUtils.isBlank(gateTicker.result) || !StringUtils.equals("true", gateTicker.result.toLowerCase())) {
            throw new IOException("gate: fetch ticker failed");
        }

        ticker.sell = gateTicker.lowestAsk;
        ticker.buy = gateTicker.highestBid;

        return ticker;
    }

    @Override
    protected String type2Symbol(Type type) {
        if (type == Type.ETH_USDT) {
            return "eth_usdt";
        }
        return null;
    }
}
