package com.paulok777;

import javafx.util.Pair;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.iterate;

public class SingleByteXorCipher {

    public Map<Integer, String> hacSingleXorCipher(String cipherText) {
        return iterate(-128, n -> n + 1)
                .limit(256)
                .map(n -> new Pair<>(n, singleXorCipher(cipherText, n.byteValue())))
//                .filter(encodedText -> Pattern.compile("[A-Za-z\\s\\d-,.:;/+=!@#&()\\[\\]]+").matcher(encodedText).matches())
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    public String singleXorCipher(String text, byte key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char res = (char) (text.charAt(i) ^ key);
            sb.append(res);
        }
        return sb.toString();
    }
}
