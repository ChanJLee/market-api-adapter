package com.chan.common.log;

import java.util.Date;

/**
 * Created by chan on 2017/9/8.
 */
public class Logger {
    private static Date sDate = new Date();

    public static void log(String tag, String message) {
        synchronized (Logger.class) {
            sDate.setTime(System.currentTimeMillis());
            System.out.println(String.format("%s %s: %s", sDate.toInstant().toString(), tag, message));
        }
    }
}
