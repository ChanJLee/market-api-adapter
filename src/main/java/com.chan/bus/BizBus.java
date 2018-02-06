package com.chan.bus;

import com.chan.api.MarketApi;
import com.chan.api.gate.GateMarketApi;
import com.chan.api.huobi.HuoBiMarketApi;
import com.chan.model.Ticker;
import com.chan.model.Type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 2018/2/6.
 */
public class BizBus extends Thread {
    private long mDuration = 5000;
    private float mBuyWarningThreshold = 7.8f;
    private float mSellWarningThreshold = 7.8f;
    private List<MarketApi> mMarketApis = new ArrayList<>();


    public BizBus(long duration) {
        mDuration = duration;
        MarketApi huoBiMarketApi = new HuoBiMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");
        mMarketApis.add(huoBiMarketApi);
        MarketApi gateMarketApi = new GateMarketApi("8299346f-17ee81c3-8b7fa980-1f1d3",
                "15342e59-a8bfd7b7-aa124530-407ff");
        mMarketApis.add(gateMarketApi);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                Thread.sleep(mDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (MarketApi marketApi : mMarketApis) {
                try {
                    Ticker ticker = marketApi.fetchTicker(Type.EOS_USDT);
                    if (ticker.buy >= mBuyWarningThreshold) {
                        waring(String.format("say %s buy price is more than %s", marketApi.market(), mBuyWarningThreshold));
                    }

                    if (ticker.sell <= mSellWarningThreshold) {
                        waring(String.format("say %s sell price is less than %s", marketApi.market(),
                                mSellWarningThreshold));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void waring(String msg) {
        try {
            Runtime.getRuntime().exec(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setBuyWarningThreshold(float buyWarningThreshold) {
        mBuyWarningThreshold = buyWarningThreshold;
    }

    public void setSellWarningThreshold(float sellWarningThreshold) {
        mSellWarningThreshold = sellWarningThreshold;
    }
}
