package com.chan.api.huobi.utils;

import org.apache.commons.lang.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by chan on 2017/9/10.
 */
public class MiscUtils {
    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
    private static final ZoneId ZONE_GMT = ZoneId.of("Z");

    public static Map<String, String> signature(String appKey, String appSecretKey, String method, String host, String uri) {
        Map<String, String> result = new HashMap<>();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.toUpperCase()).append('\n')
                .append(host.toLowerCase()).append('\n')
                .append(uri).append('\n');

        result.put("AccessKeyId", appKey);
        result.put("SignatureVersion", "2");
        result.put("SignatureMethod", "HmacSHA256");
        result.put("Timestamp", getTimestamp());

        SortedMap<String, String> map = new TreeMap<>(result);
        List<String> keyValuePair = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            keyValuePair.add(String.format("%s=%s", key, urlEncode(value)));
        }
        stringBuilder.append(StringUtils.join(keyValuePair, "&"));

        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey =
                    new SecretKeySpec(appSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }

        String payload = stringBuilder.toString();
        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        String actualSign = Base64.getEncoder().encodeToString(hash);
        result.put("Signature", actualSign);

        return result;
    }

    private static String getTimestamp() {
        return Instant.ofEpochSecond(Instant.now().getEpochSecond())
                .atZone(ZONE_GMT)
                .format(DT_FORMAT);
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }
}
