package com.paulok777.lab1;

import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Stream.iterate;

public class SingleByteXorCipher {

    public static final String TEXT_PATTERN = "[A-Za-z\"\\s\\d-,.:;/+=!@#&*?~`_^%$()}{“”\\[\\]|<>]+";

    public Map<Integer, String> hackSingleXorCipher(String cipherText) {
        return iterate(-128, n -> n + 1)
                .limit(256)
                .map(n -> new Pair<>(n, singleXorCipher(cipherText, n.byteValue())))
                .filter(pair -> Pattern.compile(TEXT_PATTERN).matcher(pair.getValue()).matches())
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private String singleXorCipher(String text, byte key) {
        byte[] encodedText = new byte[text.length()];
        for (int i = 0; i < text.length(); i++) {
            encodedText[i] = (byte) (text.charAt(i) ^ key);
        }
        return new String(encodedText, StandardCharsets.UTF_8);
    }
}
