package com.paulok777.lab1;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import static com.paulok777.lab1.SingleByteXorCipher.TEXT_PATTERN;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RepeatingKeyXorCipher {

    private static final double AVERAGE_INDEX_OF_COINCIDENCE_OF_ENGLISH = 0.06;
    private static final double AVAILABLE_UNKNOWN_SYMBOLS = 1;

    public Map<String, String> hackRepeatingXorCipher(String cipherText) {
        int lengthOfKey = getLengthOfKey(cipherText);
        List<String> keys = joinStringsInSequenceCombination(getPossibleKeys(cipherText, lengthOfKey));
        return getMapOfKeyAndDecodedString(cipherText, keys);
    }

    private Map<String, String> getMapOfKeyAndDecodedString(String cipherText, List<String> keys) {
        return keys.stream()
                .map(key -> new Pair<>(key, decipherVigenere(cipherText, key)))
                .filter(pair -> isSimilarToEnglishText(pair.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private boolean isSimilarToEnglishText(String text) {
        Map<String, Integer> lettersFrequency = new HashMap<>();

        for (String ch: text.toLowerCase().split("")) {
            if (lettersFrequency.containsKey(ch)) {
                lettersFrequency.put(ch, lettersFrequency.get(ch) + 1);
            } else {
                lettersFrequency.put(ch, 1);
            }
        }

        double frequencyOfAChar = lettersFrequency.containsKey("a") ? ((double) lettersFrequency.get("a")) / text.length() : 0.0;
        double frequencyOfEChar = lettersFrequency.containsKey("e") ? ((double) lettersFrequency.get("e")) / text.length() : 0.0;
        double frequencyOfSpaceChar = lettersFrequency.containsKey(" ") ? ((double) lettersFrequency.get(" ")) / text.length() : 0.0;

        return ((frequencyOfAChar > 0.1 || frequencyOfEChar > 0.1) && frequencyOfSpaceChar > 0.1);
    }

    private String decipherVigenere(String cipherText, String key) {
        String repeatedKey = StringUtils
                .repeat(key, (int) Math.ceil((double) cipherText.length() / key.length()))
                .substring(0, cipherText.length());

        byte[] cipherTextInBytes = textToOneByteArray(cipherText);
        byte[] repeatedKeyInBytes = repeatedKey.getBytes(UTF_8);
        byte[] decodedTextInBytes = new byte[cipherTextInBytes.length];

        for (int i = 0; i < cipherTextInBytes.length; i++) {
            decodedTextInBytes[i] = (byte) (cipherTextInBytes[i] ^ repeatedKeyInBytes[i]);
        }

        return new String(decodedTextInBytes, UTF_8);
    }

    private byte[] textToOneByteArray(String text) {
        byte[] result = new byte[text.length()];
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (currentChar > 255) {
                result[i] = " ".getBytes(UTF_8)[0];
            } else {
                result[i] = (byte) currentChar;
            }
        }

        return result;
    }

    private List<List<StringBuilder>> getPossibleKeys(String cipherText, int lengthOfKey) {
        List<List<StringBuilder>> possibleKeys = new ArrayList<>();

        for (int i = 0; i < lengthOfKey; i++) {
            List<StringBuilder> possibleBytesForKey = new ArrayList<>();

            for (int j = -128; j < 128; j++) {
                int currentNumberOfUnknownSymbols = 0;
                for (int k = i; k < cipherText.length(); k += lengthOfKey) {
                    byte xorByte = (byte) (cipherText.charAt(k) ^ j);
                    String xorByteString = new String(new byte[]{xorByte}, UTF_8);
                    if (!Pattern.compile(TEXT_PATTERN).matcher(xorByteString).matches()) {
                        currentNumberOfUnknownSymbols++;
                    }
                    if (currentNumberOfUnknownSymbols > AVAILABLE_UNKNOWN_SYMBOLS) {
                        break;
                    }
                }

                if (currentNumberOfUnknownSymbols <= AVAILABLE_UNKNOWN_SYMBOLS) {
                    String suitableByte = new String(new byte[]{(byte) j}, UTF_8);
                    possibleBytesForKey.add(new StringBuilder(suitableByte));
                }
            }

            if (possibleBytesForKey.isEmpty()) {
                throw new RuntimeException("No solutions");
            }

            possibleKeys.add(possibleBytesForKey);
        }

        return possibleKeys;
    }

    private List<String> joinStringsInSequenceCombination(List<List<StringBuilder>> possibleKeys) {
        int numberOfCombinations = possibleKeys.stream()
                .map(List::size)
                .reduce(1, (acc, next) -> acc * next);

        List<String> keys = new ArrayList<>();
        for (int i = 0; i < numberOfCombinations; i++) {
            StringBuilder key = new StringBuilder();
            for (int j = 0; j < possibleKeys.size(); j++) {
                int numberOfCombinationWithoutTemp = 1;
                List<StringBuilder> currentList = possibleKeys.get(j);

                for (int k = j + 1; k < possibleKeys.size(); k++) {
                    numberOfCombinationWithoutTemp *= possibleKeys.get(k).size();
                }

                int correctIndex = (i / numberOfCombinationWithoutTemp) % currentList.size();
                key.append(currentList.get(correctIndex));
            }
            keys.add(key.toString());
        }
        return keys;
    }

    public int getLengthOfKey(String cipherText) {
        Map<Integer, Double> indexOfCoincidence = getIndexOfCoincidenceMap(cipherText);

        List<Integer> probableKeyLength = indexOfCoincidence.entrySet().stream()
                .filter(entry -> entry.getValue() >= AVERAGE_INDEX_OF_COINCIDENCE_OF_ENGLISH)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (probableKeyLength.size() == 1) {
            return probableKeyLength.iterator().next();
        } else if (probableKeyLength.size() > 1) {
            int minDiff = probableKeyLength.iterator().next();
            for (int i = 0; i < probableKeyLength.size() - 1; i++) {
                int diff = probableKeyLength.get(i + 1) - probableKeyLength.get(i);
                if (diff < minDiff) {
                    minDiff = diff;
                }
            }
            return minDiff;
        } else {
            throw new RuntimeException("No index of coincidence");
        }
    }

    private Map<Integer, Double> getIndexOfCoincidenceMap(String cipherText) {
        Map<Integer, Double> indexOfCoincidenceMap = new HashMap<>();

        for (int i = 1; i < cipherText.length(); i++) {
            int numberOfMatches = 0;
            String shiftString = cipherText.substring(i) + cipherText.substring(0, i);
            for (int j = 0; j < cipherText.length(); j++) {
                if (cipherText.charAt(j) == shiftString.charAt(j)) {
                    numberOfMatches++;
                }
            }
            double indexOfCoincidence = (double) numberOfMatches / cipherText.length();
            indexOfCoincidenceMap.put(i, indexOfCoincidence);
        }

        return indexOfCoincidenceMap;
    }
}
