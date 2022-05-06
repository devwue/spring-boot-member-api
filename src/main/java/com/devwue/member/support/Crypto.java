package com.devwue.member.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class Crypto {
    private Crypto() {}

    public static String encrypt(String plainText, String securityKey, String iv) {
        try {
            return encryptAes(plainText, securityKey, iv);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException |NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("crypto encrypt: {}  - {}", e.getCause(), e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("crypto encrypt iv: {}  - {}", e.getCause(), e.getMessage(), e);
        }
        return null;
    }

    public static String decrypt(String encryptedText, String securityKey, String iv)  {
        try {
            return decryptAes(encryptedText, securityKey, iv);
        } catch (IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException |NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("crypto decrypt: {}  - {}", e.getCause(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("crypto decrypt iv: {}  - {}", e.getCause(), e.getMessage(), e);
        }
        return encryptedText;
    }

    private static SecretKeySpec makeSecretKey(String securityKey) {
        SecretKeySpec secretKeySpec = null;
        try {
            final byte[] key = securityKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            secretKeySpec = new SecretKeySpec(Arrays.copyOf(sha.digest(key), 16),"AES");
        } catch (NoSuchAlgorithmException e) {
            log.error("crypto makeSecretKey exception {} - {}", e.getClass(), e.getMessage(), e);
        }
        return secretKeySpec;
    }

    private static String encryptAes(String text, String securityKey, String iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, makeSecretKey(securityKey), new IvParameterSpec(iv.getBytes()));

        return Base64Utils.encodeToString(cipher.doFinal(text.getBytes()));
    }

    private static String decryptAes(String text, String securityKey, String iv) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, makeSecretKey(securityKey), new IvParameterSpec(iv.getBytes()));

        return new String(cipher.doFinal(Base64Utils.decodeFromString(text)));
    }
}
