package com.paulok777.lab4;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private static final int ITERATIONS = 1;
    private static final int MEMORY = 16384;
    private static final int PARALLELISM = 4;

    public String getMd5Hash(String text) throws NoSuchAlgorithmException {
        return getMd5Hash(text, null);
    }

    public String getMd5Hash(String text, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        if (salt != null) {
            md5Digest.update(salt);
        }

        byte[] hash = md5Digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return byteArrayToHex(hash);
    }

    public String getArgonHash(String text) {
        Argon2 argon2 = Argon2Factory.create();
        return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, text.getBytes(StandardCharsets.UTF_8));
    }

    private String byteArrayToHex(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            String hex = Integer.toHexString(Byte.toUnsignedInt(b));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }
        }
        return sb.toString();
    }
}
