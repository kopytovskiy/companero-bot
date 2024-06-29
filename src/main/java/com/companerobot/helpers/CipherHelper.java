package com.companerobot.helpers;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.companerobot.constants.ExecutionConstants.CIPHER_ALGORITHM;
import static com.companerobot.constants.ExecutionConstants.CIPHER_KEY;

public class CipherHelper {

    public static String encrypt(String inputText) {

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, convertStringToSecretKey(CIPHER_KEY));
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] cipherText;
        try {
            cipherText = cipher.doFinal(inputText.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String encryptedText)  {

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, convertStringToSecretKey(CIPHER_KEY));
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] plainText;
        try {
            plainText = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(plainText);
    }

    public static SecretKey convertStringToSecretKey(String stringKey) {
        byte[] decodedKey = Base64.getDecoder().decode(stringKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

//    public static SecretKey generateKey(int size) {
//        KeyGenerator keyGenerator;
//        try {
//            keyGenerator = KeyGenerator.getInstance("AES");
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//        keyGenerator.init(size); //256
//        return keyGenerator.generateKey();
//    }

}
