package com.chan.api;

import com.chan.model.Ticker;
import com.chan.model.Type;

import java.io.IOException;

/**
 * Created by chan on 2017/11/14.
 */
public interface MarketApi {
    Ticker fetchTicker(Type type) throws IOException;
}
