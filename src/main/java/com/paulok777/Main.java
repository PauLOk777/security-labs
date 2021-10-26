package com.paulok777;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
//        String text = "My name is Pavlo. I am 20 years old. I have nice friends";
        String text = "7958401743454e1756174552475256435e59501a5c524e176f786517545e475f5245191772195019175e4317445f58425b531743565c521756174443455e595017d5b7ab5f525b5b58174058455b53d5b7aa175659531b17505e41525917435f52175c524e175e4417d5b7ab5c524ed5b7aa1b174f584517435f5217515e454443175b524343524517d5b7ab5fd5b7aa17405e435f17d5b7ab5cd5b7aa1b17435f5259174f584517d5b7ab52d5b7aa17405e435f17d5b7ab52d5b7aa1b17435f525917d5b7ab5bd5b7aa17405e435f17d5b7ab4ed5b7aa1b1756595317435f5259174f58451759524f4317545f564517d5b7ab5bd5b7aa17405e435f17d5b7ab5cd5b7aa175650565e591b17435f525917d5b7ab58d5b7aa17405e435f17d5b7ab52d5b7aa1756595317445817585919176e5842175a564e17424452175659175e5953524f1758511754585e59545e53525954521b177f565a5a5e595017535e4443565954521b177c56445e445c5e17524f565a5e5956435e58591b17444356435e44435e54565b17435244434417584517405f564352415245175a52435f5853174e5842175152525b174058425b5317445f584017435f52175552444317455244425b4319";
        String asciiText = convertHex(text, StandardCharsets.UTF_8);
        SingleByteXorCipher singleByteXorCipher = new SingleByteXorCipher();
        Map<Integer, String> texts = singleByteXorCipher.hacSingleXorCipher(asciiText);
        printMap(texts);
    }

    public static String convertHex(String hex, Charset charset) {
        if (hex.length() % 2 != 0) {
            throw new RuntimeException("Invalid Hex");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hex.length(); i += 2) {
            String oneByte = hex.substring(i, i + 2);
            int number = Integer.valueOf(oneByte, 16);
            sb.append((char) number);
        }

        return new String(sb.toString().getBytes(), charset);
    }

    public static void printMap(Map<?, ?> map) {
        for (Object key : map.keySet()) {
            System.out.println("Key: " + key + ", Value: " + map.get(key));
        }
    }
}
