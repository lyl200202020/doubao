package com.company.admin.common.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import java.nio.charset.StandardCharsets;

public class SM3Calculator {
    
    public static String hash(String data) {
        if (data == null) {
            return null;
        }
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        SM3Digest digest = new SM3Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        System.out.println("SM3('123456') = " + hash("123456"));
        System.out.println("SM3('admin123') = " + hash("admin123"));
    }
}
