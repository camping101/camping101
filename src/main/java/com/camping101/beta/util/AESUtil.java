package com.camping101.beta.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Logger;

@Component
public class AESUtil {

    private byte[] key;
    private SecretKeySpec secretKeySpec;
    private final Logger logger = Logger.getLogger(AESUtil.class.getName());

    @Autowired
    public AESUtil(@Value("${jasypt.encryptor.password}") String rawKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = rawKey.getBytes(StandardCharsets.UTF_8);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 24);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(x -> logger.warning(x.toString()));
        }
    }

    public String encrypt(String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return encodeBase64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(decodeBase64(str)));
    }

    private String encodeBase64(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    private byte[] decodeBase64(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }
}
