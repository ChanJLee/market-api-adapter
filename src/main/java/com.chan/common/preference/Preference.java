package com.chan.common.preference;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chan on 2017/9/9.
 */
public class Preference {
    private static Preference sPreference;
    private Map<String, String> mMap = new HashMap<>();

    public Preference(File configFile) {
        if (!configFile.exists()) {
            throw new RuntimeException("can not open " + configFile.getAbsolutePath());
        }

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(configFile);
            List<String> lines = IOUtils.readLines(fileInputStream, Charset.forName("UTF-8"));
            for (String line : lines) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] pair = line.split("=");
                if (pair.length != 2) {
                    continue;
                }
                mMap.put(pair[0], pair[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public int getInt(String key, int defaultValue) {
        String value = mMap.get(key);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public String getString(String key) {
        return mMap.get(key);
    }

    public static Preference getInstance(File config) {
        if (sPreference == null) {
            synchronized (Preference.class) {
                if (sPreference == null) {
                    sPreference = new Preference(config);
                }
            }
        }
        return sPreference;
    }
}
