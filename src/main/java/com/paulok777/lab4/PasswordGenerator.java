package com.paulok777.lab4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.IntStream.iterate;

public class PasswordGenerator {

    private static final double MOST_COMMON_25_PASSWORDS_CHANCE = 0.05;
    private static final double REALLY_RANDOM_CHANCE = 0.05;
    private static final double CUSTOM_GENERATOR_CHANCE = 0.10;
    private static final double SPECIAL_CHARACTER_CHANCE = 0.1;
    private static final double NUMBER_CHANCE = 0.4;
    private static final int MIN_PASSWORD_SIZE = 16;
    private static final int MAX_PASSWORD_SIZE = 24;

    private static final List<Character> LETTERS = new ArrayList<>();
    private static final List<Character> NUMBERS = new ArrayList<>();
    private static final List<Character> SPECIAL_CHARACTERS = new ArrayList<>();
    private static final List<String> COMBINATION_TO_PASTE_AT_THE_END = asList(
            "1", "!", "2", "3", "4", "5", "6", "7", "8", "9", "0", "1990", "1991", "1992", "1993", "1994",
            "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
            "I", "i", "$", "%", "^", "&", "?", "<", ">", "<3", "333", "22", "444"
    );
    private static final Map<Character, String> SWAP_MAP = new HashMap<>();

    private SecureRandom secureRandom = new SecureRandom();

    public PasswordGenerator() {
        for (char i = 'a'; i <= 'z'; i++) {
            LETTERS.add(i);
        }

        for (char i = 'A'; i <= 'Z'; i++) {
            LETTERS.add(i);
        }

        for (char i = '0'; i <= '9'; i++) {
            NUMBERS.add(i);
        }

        for (char i = '!'; i <= '/'; i++) {
            SPECIAL_CHARACTERS.add(i);
        }

        for (char i = ':'; i <= '@'; i++) {
            SPECIAL_CHARACTERS.add(i);
        }

        for (char i = 92; i <= '`'; i++) {
            SPECIAL_CHARACTERS.add(i);
        }

        for (char i = '{'; i <= '~'; i++) {
            SPECIAL_CHARACTERS.add(i);
        }

        SWAP_MAP.put('o', "0");
        SWAP_MAP.put('O', "0");
        SWAP_MAP.put('i', "1");
        SWAP_MAP.put('l', "1");
        SWAP_MAP.put('I', "1");
        SWAP_MAP.put('a', "@");
        SWAP_MAP.put('e', "3");
        SWAP_MAP.put('E', "3");
        SWAP_MAP.put('s', "$");
        SWAP_MAP.put('b', "6");
        SWAP_MAP.put('L', "|_");
        SWAP_MAP.put('1', "!");
        SWAP_MAP.put('k', "|<");
        SWAP_MAP.put('K', "|<");
        SWAP_MAP.put('H', "|-|");
    }

    public List<String> generateBunchOfPasswords(int numberOfPasswords) {
        return iterate(0, x -> x)
                .limit(numberOfPasswords)
                .mapToObj(x -> generatePassword())
                .collect(Collectors.toList());
    }

    public String generatePassword() {
        double randomValue = secureRandom.nextDouble();
        if (randomValue < MOST_COMMON_25_PASSWORDS_CHANCE) {
            return getRandomPasswordFromTop25();
        } else if (randomValue < MOST_COMMON_25_PASSWORDS_CHANCE + REALLY_RANDOM_CHANCE) {
            return generateReallyRandomPassword();
        } else if (randomValue < MOST_COMMON_25_PASSWORDS_CHANCE + REALLY_RANDOM_CHANCE + CUSTOM_GENERATOR_CHANCE) {
            return generateCustomPassword();
        } else {
            return getRandomPasswordFrom100KCommon();
        }
    }

    private String getRandomPasswordFromTop25() {
        try {
            return Files.readAllLines(Paths.get("top25passwords.txt"))
                    .get(secureRandom.nextInt(25)).split(". ")[1];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRandomPasswordFrom100KCommon() {
        try (Stream<String> lines = Files.lines(Paths.get("100kMostCommonPasswords.txt"))) {
            return lines.skip(secureRandom.nextInt(100000)).findFirst().orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateCustomPassword() {
        String password = getRandomPasswordFrom100KCommon() +
                COMBINATION_TO_PASTE_AT_THE_END.get(secureRandom.nextInt(COMBINATION_TO_PASTE_AT_THE_END.size()));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            char currentChar = password.charAt(i);
            if (SWAP_MAP.containsKey(currentChar)) {
                sb.append(SWAP_MAP.get(currentChar));
            } else {
                sb.append(currentChar);
            }
        }
        return sb.toString();
    }

    private String generateReallyRandomPassword() {
        int passwordLength = secureRandom.nextInt(MAX_PASSWORD_SIZE - MIN_PASSWORD_SIZE) + MIN_PASSWORD_SIZE;
        StringBuilder sb = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; i++) {
            double randomValue = secureRandom.nextDouble();
            if (randomValue < SPECIAL_CHARACTER_CHANCE) {
                sb.append(SPECIAL_CHARACTERS.get(secureRandom.nextInt(SPECIAL_CHARACTERS.size())));
            } else if (randomValue < SPECIAL_CHARACTER_CHANCE + NUMBER_CHANCE) {
                sb.append(NUMBERS.get(secureRandom.nextInt(NUMBERS.size())));
            } else {
                sb.append(LETTERS.get(secureRandom.nextInt(LETTERS.size())));
            }
        }
        return sb.toString();
    }
}
