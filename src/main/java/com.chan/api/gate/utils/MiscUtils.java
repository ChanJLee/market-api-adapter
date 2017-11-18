package com.chan.api.gate.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MiscUtils {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String buildMysignV1(Map<String, String> sArray,
                                       String secretKey) {
        String mysign = "";
        try {
            String prestr = createLinkString(sArray);
            prestr = prestr + "&secret_key=" + secretKey;
            mysign = getMD5String(prestr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mysign;
    }


    private static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    private static String getMD5String(String str) {
        try {
            if (str == null || str.trim().length() == 0) {
                return "";
            }
            byte[] bytes = str.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >> 4] + ""
                        + HEX_DIGITS[bytes[i] & 0xf]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String signature(Map<String, String> args, String secretKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

        Mac mac = null;
        SecretKeySpec key = null;
        String postData = "";

        List<String> argKVList = new ArrayList<>();
        for (Map.Entry<String, String> arg : args.entrySet()) {
            argKVList.add(String.format("%s=%s", arg.getKey(), arg.getValue()));
        }

        postData = StringUtils.join(argKVList, "&");
        key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA512");
        mac = Mac.getInstance("HmacSHA512");
        mac.init(key);
        return Hex.encodeHexString(mac.doFinal(postData.getBytes("UTF-8")));
    }
}
