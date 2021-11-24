package com.paulok777.lab2;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

import static com.paulok777.lab1.Main.convertHex;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    public static void main(String[] args) {
        // 1-2 stroka = Th'oppressor's wrong, the proud ma(2),For who would bear the whips and s(1)
        // 1-3 stroka = For(1), The pangs of dispriz'd love, the l
        // 1-4 stroka = The insolence of office, and the s (4)
        // 1-5 stroka = t patient merit of th'unworthy (5)
        // 1-5 stroka = When he himself might his quietus (5)
        // 1-7 stroka = h a bare bodkin? Who would fard(7)
        // 1-16 stroka = sicklied o'er with the pale cas(16)
        // William Shakespeare
//        String hexOfTwoMessages1And2 = "1207554F07181D45041C1A1E43534212131D4E134445541F0D4900014F140A444D120D48014E104F011255190C011C00";
//        String hexOfTwoMessages1And3 ="1207170007090147044F1A0A44440B161100490E4F01001B071F155F00150601001F0218551D53440A0A410D45BBE0B5";
        String hexOfTwoMessages1And4 ="120717001E061C4F1B0A1B0F01000D03411D46120106455B48081E17001506010000131A000000647FAA74BB9CCF6677";
//        String hexOfTwoMessages1And5 ="1207135457180E541E0A1B18444D07170806001B0E45541F4F1C1E044F131A0C5953170E190B000C4F7E4A6A81AAB6FC";
//        String hexOfTwoMessages1And6 ="1107174E57000A001F06181F014C04450C1B471C1C45481E1B49010649041A1153530E0E190B5B54ABDEB8E9F142F29B";
//        String hexOfTwoMessages1And7 ="1106064857094F42161D104C064F060E081C1F543F0D4F571F06051F4441080552170603014E11450E140CE3A24E4643";
//        String hexOfTwoMessages1And16 ="0F1C52531E0B044C1E0A114C0B070717410549000045541F0D4900124C044E074100174F1D085354070955130119494C";
        String hexOfTheWord = "466f722077686f20776f756c6420626561722074686520776869707320616e642073";

        for (int i = 0; i < hexOfTwoMessages1And4.length() - hexOfTheWord.length(); i += 2) {
            String xor = xorHex(hexOfTheWord, hexOfTwoMessages1And4.substring(i, i + hexOfTheWord.length()));
            System.out.println(i / 2 + ":" + convertHex(xor, UTF_8));
        }

//        System.out.println(hexOfTwoMessages.substring(68));
    }

    public static String xorHex(String hex1, String hex2) {
        byte[] result = new byte[hex1.length() / 2];
        for (int i = 0; i < hex1.length(); i += 2) {
            int hex1InByte = Integer.valueOf(hex1.substring(i, i + 2), 16);
            int hex2InByte = Integer.valueOf(hex2.substring(i, i + 2), 16);
            result[i / 2] = (byte) (hex1InByte ^ hex2InByte);
        }

        String resultString = new BigInteger(1, result).toString(16);
        if (resultString.length() < hex1.length()) {
            return StringUtils.repeat("0", hex1.length() - resultString.length()) + resultString;
        } else {
            return resultString;
        }
    }
}
