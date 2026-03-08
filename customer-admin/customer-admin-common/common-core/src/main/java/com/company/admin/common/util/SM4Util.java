package com.company.admin.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

public class SM4Util {
    
    private static final String ALGORITHM = "SM4/CBC/PKCS5Padding";
    private static final String ALGORITHM_NAME = "SM4";
    private static final int KEY_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    
    private static final String DEFAULT_KEY = "customer-admin-sm4-key-2024";
    
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static String encrypt(String data) {
        return encrypt(data, DEFAULT_KEY);
    }

    public static String encrypt(String data, String key) {
        if (data == null || key == null) {
            throw new IllegalArgumentException("data and key cannot be null");
        }
        try {
            byte[] keyBytes = getValidKey(key.getBytes(StandardCharsets.UTF_8));
            byte[] iv = generateIV();
            byte[] encrypted = encrypt(data.getBytes(StandardCharsets.UTF_8), keyBytes, iv);
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("SM4 encryption failed", e);
        }
    }

    public static String decrypt(String encryptedData) {
        return decrypt(encryptedData, DEFAULT_KEY);
    }

    public static String decrypt(String encryptedData, String key) {
        if (encryptedData == null || key == null) {
            throw new IllegalArgumentException("encryptedData and key cannot be null");
        }
        try {
            byte[] keyBytes = getValidKey(key.getBytes(StandardCharsets.UTF_8));
            byte[] combined = Base64.getDecoder().decode(encryptedData);
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, encrypted, 0, encrypted.length);
            byte[] decrypted = decrypt(encrypted, keyBytes, iv);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("SM4 decryption failed", e);
        }
    }

    public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] encryptedData, byte[] key, byte[] iv) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        return cipher.doFinal(encryptedData);
    }

    private static byte[] getValidKey(byte[] key) {
        if (key.length >= KEY_LENGTH) {
            byte[] validKey = new byte[KEY_LENGTH];
            System.arraycopy(key, 0, validKey, 0, KEY_LENGTH);
            return validKey;
        }
        byte[] paddedKey = new byte[KEY_LENGTH];
        System.arraycopy(key, 0, paddedKey, 0, key.length);
        return paddedKey;
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        for (int i = 0; i < IV_LENGTH; i++) {
            iv[i] = 0;
        }
        return iv;
    }
}
