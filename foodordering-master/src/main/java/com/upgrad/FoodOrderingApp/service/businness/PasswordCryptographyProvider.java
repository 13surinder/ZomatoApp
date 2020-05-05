package com.upgrad.FoodOrderingApp.service.businness;


import org.springframework.stereotype.Component;

import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordCryptographyProvider {

    private static String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA512";
    private static int HASHING_ITERATIONS = 1000;
    private static int HASHING_KEY_LENGTH = 64;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * This method generates Salt and hashed Password
     *
     * @param pass char array.
     * @return String array with [0] encoded salt [1] hashed pass.
     */
    public String[] encrypt(final String pass) {
        byte[] salt = generateSaltBytes();
        byte[] hashedPassword = hashPassword(pass.toCharArray(), salt);
        return new String[]{getBase64EncodedBytesAsString(salt), bytesToHex(hashedPassword)};
    }

    /**
     * This method re-generates hashed Password from raw-pass and salt.
     * This will be used during authentication.
     *
     * @param pass char array.
     * @param salt     byte array.
     * @return byte array of hashed password.
     */
    public static String encrypt(final String pass, String salt) {
        return bytesToHex(hashPassword(pass.toCharArray(), getBase64DecodedStringAsBytes(salt)));
    }

    /**
     * This method generates Salt
     *
     * @return 32 bytes long array
     */
    private static byte[] generateSaltBytes() {
        final Random rand = new SecureRandom();
        byte[] saltByte = new byte[32];
        rand.nextBytes(saltByte);
        return saltByte;
    }

    /**
     * This method generates hashed Password
     *
     * @param pass char array.
     * @param salt     byte array.
     * @return byte array of hashed pass.
     */
    private static byte[] hashPassword(final char[] pass, final byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            PBEKeySpec spec = new PBEKeySpec(pass, salt, HASHING_ITERATIONS, HASHING_KEY_LENGTH);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] byteArr) {
        char[] hexCharsArr = new char[byteArr.length * 2];
        for (int j = 0; j < byteArr.length; j++) {
            int v = byteArr[j] & 0xFF;
            hexCharsArr[j * 2] = hexArray[v >>> 4];
            hexCharsArr[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexCharsArr);
    }

    private static String getBase64EncodedBytesAsString(byte byteArr[]) {
        return Base64.getEncoder().encodeToString(byteArr);
    }

    private static byte[] getBase64DecodedStringAsBytes(String decode) {
        return Base64.getDecoder().decode(decode);
    }
}


