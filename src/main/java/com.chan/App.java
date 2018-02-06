package com.chan;

import com.chan.bus.BizBus;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by chan on 2017/11/22.
 */
public class App {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            return;
        }

        if (StringUtils.equals("-h", args[0])) {
            System.out.println("-h 帮助");
            System.out.println("start -d duration -b price -s price");
            return;
        }

        BizBus bizBus = new BizBus();
        if (StringUtils.equals("start", args[0])) {
            dispatchSetting(bizBus, args, 1);
        }

        bizBus.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String command = bufferedReader.readLine();
            dispatchCommand(bizBus, command);
        }
    }

    private static void dispatchCommand(BizBus bizBus, String command) {
        dispatchSetting(bizBus, command.split(" "), 0);
    }

    private static void dispatchSetting(BizBus bizBus, String[] args, int start) {
        for (int i = start; i < args.length; ++i) {
            if (StringUtils.equals("-d", args[i]) && i + 1 < args.length) {
                setBusDuration(bizBus, args[i + 1]);
                i += 2;
            } else if (StringUtils.equals("-b", args[i]) && i + 1 < args.length) {
                setBusBuyPriceThreshold(bizBus, args[i + 1]);
                i += 2;
            } else if (StringUtils.equals("-s", args[i]) && i + 1 < args.length) {
                setBusSellPriceThreshold(bizBus, args[i + 1]);
                i += 2;
            }
        }
    }

    private static void setBusDuration(BizBus bizBus, String duration) {
        try {
            bizBus.setDuration(Long.valueOf(duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setBusBuyPriceThreshold(BizBus bizBus, String price) {
        try {
            bizBus.setBuyWarningThreshold(Float.valueOf(price));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setBusSellPriceThreshold(BizBus bizBus, String price) {
        try {
            bizBus.setSellWarningThreshold(Float.valueOf(price));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}