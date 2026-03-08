package com.company.admin.common.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SM3Util {
    
    private static final String ENCODING = "UTF-8";

    public static String hash(String data) {
        if (data == null) {
            return null;
        }
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] hash = hash(bytes);
        return bytesToHex(hash);
    }

    public static byte[] hash(byte[] data) {
        SM3Digest digest = new SM3Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    public static String hmac(String key, String data) {
        if (key == null || data == null) {
            return null;
        }
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] hmac = hmac(keyBytes, dataBytes);
        return bytesToHex(hmac);
    }

    public static byte[] hmac(byte[] key, byte[] data) {
        HMac hMac = new HMac(new SM3Digest());
        hMac.init(new KeyParameter(key));
        hMac.update(data, 0, data.length);
        byte[] hmac = new byte[hMac.getMacSize()];
        hMac.doFinal(hmac, 0);
        return hmac;
    }

    public static boolean verify(String data, String hashValue) {
        if (data == null || hashValue == null) {
            return false;
        }
        String dataHash = hash(data);
        return dataHash != null && constantTimeEquals(dataHash, hashValue);
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);
        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        return result == 0;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[0];
        }
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);
        return bytesToHex(salt);
    }

    public static String hashWithSalt(String data, String salt) {
        return hash(salt + data + salt);
    }
}
