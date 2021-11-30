package com.paulok777.lab4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int NUMBER_OF_PASSWORDS = 100000;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        createCsvWithMd5AndArgon2Hashes(NUMBER_OF_PASSWORDS);
    }

    public static void createCsvWithMd5AndArgon2Hashes(int numberOfHashes) throws NoSuchAlgorithmException {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        HashGenerator hashGenerator = new HashGenerator();

        List<String> passwordsForMd5 = passwordGenerator.generateBunchOfPasswords(numberOfHashes);
        List<String> passwordsForArgon2 = passwordGenerator.generateBunchOfPasswords(numberOfHashes);
        List<String> md5Hashes = new ArrayList<>();
        List<String> argon2Hashes = new ArrayList<>();

        for (String s : passwordsForMd5) {
            md5Hashes.add(hashGenerator.getMd5Hash(s));
        }

        for (String s : passwordsForArgon2) {
            argon2Hashes.add("\"" + hashGenerator.getArgonHash(s) + "\"");
        }

        writeToFile(md5Hashes, new File("md5Hashes.csv"));
        writeToFile(argon2Hashes, new File("argon2Hashes.csv"));
    }

    public static void writeToFile(List<String> rows, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            for(String str: rows) {
                writer.write(str + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
